// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import dyna.framework.service.dos.DOSChangeable;
import java.util.ArrayList;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service:
//            OLM

public interface DOS
    extends Service
{

    public abstract void ping();

    public abstract String getTimeToken()
        throws IIPRequestException;

    public abstract long generateOUID()
        throws IIPRequestException;

    public abstract String createModel(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeModel(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getModel(String s)
        throws IIPRequestException;

    public abstract void setModel(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listModel()
        throws IIPRequestException;

    public abstract ArrayList listRootClassInModel(String s)
        throws IIPRequestException;

    public abstract void setWorkingModel(String s)
        throws IIPRequestException;

    public abstract String getWorkingModel()
        throws IIPRequestException;

    public abstract void releaseWorkingModel()
        throws IIPRequestException;

    public abstract String createPackage(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String createPackage(String s, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removePackage(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getPackage(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getPackageWithName(String s)
        throws IIPRequestException;

    public abstract void setPackage(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listClassInPackage(String s)
        throws IIPRequestException;

    public abstract ArrayList listPackage()
        throws IIPRequestException;

    public abstract ArrayList listPackage(String s)
        throws IIPRequestException;

    public abstract ArrayList listAllPackage()
        throws IIPRequestException;

    public abstract void linkPackageAndModel(String s, String s1)
        throws IIPRequestException;

    public abstract void unlinkPackageAndModel(String s, String s1)
        throws IIPRequestException;

    public abstract String createClass(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeClass(String s)
        throws IIPRequestException;

    public abstract void removeClassCascade(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getClass(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getClassWithName(String s, String s1)
        throws IIPRequestException;

    public abstract void setClass(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listSuperClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listAllSuperClassOuid(String s)
        throws IIPRequestException;

    public abstract ArrayList listSubClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listSubClassInModel(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList listAllSubClassOuid(String s)
        throws IIPRequestException;

    public abstract ArrayList listAllLeafClassOuid(String s)
        throws IIPRequestException;

    public abstract ArrayList listAllLeafClassOuidInModel(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList listAllLeafClassOuidInModel(String s)
        throws IIPRequestException;

    public abstract ArrayList listFieldInClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listActionInClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listFieldGroupInClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listAssociationOfClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listClass()
        throws IIPRequestException;

    public abstract String createField(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeField(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getField(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getFieldWithName(String s, String s1)
        throws IIPRequestException;

    public abstract void setField(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String getClassOuidOfField(String s)
        throws IIPRequestException;

    public abstract ArrayList listField()
        throws IIPRequestException;

    public abstract void setCodeSelectionTable(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList getCodeSelectionTable(String s)
        throws IIPRequestException;

    public abstract boolean isComboBoxMatrixCode(String s)
        throws IIPRequestException;

    public abstract String lookupCodeFromSelectionTable(String s, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupCodeFromSelectionTable(String s, String s1, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupInitialCodeFromSelectionTable(String s, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String createAction(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeAction(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getAction(String s)
        throws IIPRequestException;

    public abstract void setAction(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String getClassOuidOfAction(String s)
        throws IIPRequestException;

    public abstract ArrayList listAction()
        throws IIPRequestException;

    public abstract void createActionParameter(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeActionParameter(String s, String s1)
        throws IIPRequestException;

    public abstract DOSChangeable getActionParameter(String s, String s1)
        throws IIPRequestException;

    public abstract void setActionParameter(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listActionParameterInClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listActionParameter()
        throws IIPRequestException;

    public abstract String getActionScriptName(String s)
        throws IIPRequestException;

    public abstract boolean setActionScript(String s, String s1)
        throws IIPRequestException;

    public abstract boolean removeActionScript(String s)
        throws IIPRequestException;

    public abstract ArrayList listObjectUseScript(String s, String s1)
        throws IIPRequestException;

    public abstract boolean removeScript(String s, String s1)
        throws IIPRequestException;

    public abstract String createFieldGroup(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeFieldGroup(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getFieldGroup(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getFieldGroupWithName(String s, String s1)
        throws IIPRequestException;

    public abstract void setFieldGroup(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listFieldInFieldGroup(String s)
        throws IIPRequestException;

    public abstract ArrayList listActionInFieldGroup(String s)
        throws IIPRequestException;

    public abstract ArrayList listFieldGroup()
        throws IIPRequestException;

    public abstract String createAssociation(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeAssociation(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getAssociation(String s)
        throws IIPRequestException;

    public abstract void setAssociation(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract DOSChangeable getAssociationEnd(String s, int i)
        throws IIPRequestException;

    public abstract void setAssociationEnd(DOSChangeable doschangeable, int i)
        throws IIPRequestException;

    public abstract String getAssociationEndClass(String s, int i)
        throws IIPRequestException;

    public abstract void setAssociationEndClass(String s, int i, String s1)
        throws IIPRequestException;

    public abstract ArrayList listAssociation()
        throws IIPRequestException;

    public abstract String add(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void remove(String s)
        throws IIPRequestException;

    public abstract DOSChangeable get(String s)
        throws IIPRequestException;

    public abstract void set(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract DOSChangeable getFieldValue(String s)
        throws IIPRequestException;

    public abstract void setFieldValue(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract DOSChangeable getCoreFieldValue(String s)
        throws IIPRequestException;

    public abstract String getStatus(String s)
        throws IIPRequestException;

    public abstract void setStatus(String s, String s1)
        throws IIPRequestException;

    public abstract void lock(String s)
        throws IIPRequestException;

    public abstract void unlock(String s)
        throws IIPRequestException;

    public abstract HashMap cloneObject(String s, String s1, HashMap hashmap)
        throws IIPRequestException;

    public abstract String makeWipObject(String s)
        throws IIPRequestException;

    public abstract ArrayList list(String s, ArrayList arraylist, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList list(String s)
        throws IIPRequestException;

    public abstract ArrayList list(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList list(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract void link(String s, String s1)
        throws IIPRequestException;

    public abstract void unlink(String s, String s1)
        throws IIPRequestException;

    public abstract String link(String s, String s1, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void unlink(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract ArrayList listLink(String s, boolean flag, ArrayList arraylist, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listLinkFrom(String s, ArrayList arraylist, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listLinkFrom(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList listLinkFrom(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listLinkFrom(String s)
        throws IIPRequestException;

    public abstract ArrayList listLinkForCADIntegration(String s)
        throws IIPRequestException;

    public abstract ArrayList listLinkTo(String s, ArrayList arraylist, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listLinkTo(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList listLinkTo(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listLinkTo(String s)
        throws IIPRequestException;

    public abstract ArrayList getExpanedList(String s, boolean flag, ArrayList arraylist, HashMap hashmap)
        throws IIPRequestException;

    public abstract String generateNumber(String s)
        throws IIPRequestException;

    public abstract String getClassOuid(String s)
        throws IIPRequestException;

    public abstract ArrayList listFile(String s)
        throws IIPRequestException;

    public abstract String attachFile(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean detachFile(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract String checkoutFile(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract String checkinFile(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean setFileDescription(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList getListVersionHistoryOuid(String s)
        throws IIPRequestException;

    public abstract ArrayList getListVersionHistory(String s)
        throws IIPRequestException;

    public abstract ArrayList listEventForModel(String s)
        throws IIPRequestException;

    public abstract ArrayList listEvent(String s)
        throws IIPRequestException;

    public abstract boolean removeEvent(String s, String s1)
        throws IIPRequestException;

    public abstract boolean setEvent(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract String getEventName(String s, String s1)
        throws IIPRequestException;

    public abstract String createCode(String s, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String createCode(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeCode(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getCode(String s)
        throws IIPRequestException;

    public abstract void setCode(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listCode(String s)
        throws IIPRequestException;

    public abstract ArrayList listCode()
        throws IIPRequestException;

    public abstract DOSChangeable getCodeWithName(String s)
        throws IIPRequestException;

    public abstract String createCodeItem(String s, DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeCodeItem(String s)
        throws IIPRequestException;

    public abstract void removeCodeItemInCode(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getCodeItem(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getCodeItemWithName(String s, String s1)
        throws IIPRequestException;

    public abstract DOSChangeable getCodeItemWithId(String s, String s1)
        throws IIPRequestException;

    public abstract void setCodeItem(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listCodeItem(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getCodeItemRoot(String s)
        throws IIPRequestException;

    public abstract ArrayList getCodeItemChildren(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getCodeItemParent(String s)
        throws IIPRequestException;

    public abstract void setInstanceCachSize(int i)
        throws IIPRequestException;

    public abstract void clearInstanceCache()
        throws IIPRequestException;

    public abstract int getInstanceCacheSize()
        throws IIPRequestException;

    public abstract int getInstanceCacheContentSize()
        throws IIPRequestException;

    public abstract HashMap listEffectiveAssociation(String s, Boolean boolean1, String s1)
        throws IIPRequestException;

    public abstract void createCADAttribute(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void setCADAttribute(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeCADAttribute(String s)
        throws IIPRequestException;

    public abstract ArrayList listCADAttribute(String s)
        throws IIPRequestException;

    public abstract void saveCADAttributePosition(ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList listCADAttributePosition(String s)
        throws IIPRequestException;

    public abstract HashMap maxAttributeNameAndIndex(String s)
        throws IIPRequestException;

    public abstract ArrayList listInheritedFieldInFieldGroup(String s)
        throws IIPRequestException;

    public abstract void setOLMSerivceInstance(OLM olm)
        throws IIPRequestException;

    public abstract ArrayList advancedList(ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList executeQuery(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract boolean checkDuplicateSequence(String s, String s1, String s2, String s3)
        throws IIPRequestException;

    public abstract void setVersionString()
        throws IIPRequestException;

    public abstract ArrayList getVersionStringArray()
        throws IIPRequestException;

    public abstract String checkDuplicateNumber(String s, String s1)
        throws IIPRequestException;

    public abstract void cancelCheckoutFile(String s, HashMap hashmap)
        throws IIPRequestException;

    public static final int SCOPE_CLASSIFIER = 1;
    public static final int SCOPE_INSTANCE = 2;
    public static final int CHANGEABLE_CHANGEABLE = 1;
    public static final int CHANGEABLE_ADD_ONLY = 2;
    public static final int CHANGEABLE_FROZEN = 3;
    public static final int CHANGEABLE_READ_ONLY = 4;
    public static final int CALL_GUARDED = 1;
    public static final int CALL_CONCURRENT = 2;
    public static final int ASSOCIATION_ASSOCIATION = 1;
    public static final int ASSOCIATION_COMPOSITION = 2;
    public static final int ASSOCIATION_AGGREGATION = 3;
    public static final byte TYPE_STATUS = -15;
    public static final byte TYPE_USER = -14;
    public static final byte TYPE_COLLECTION = -13;
    public static final String EVENT_ADD_BEFORE = "add.before";
    public static final String EVENT_ADD_AFTER = "add.after";
    public static final String EVENT_ADD_INIT = "add.init";
    public static final String EVENT_REMOVE_BEFORE = "remove.before";
    public static final String EVENT_REMOVE_AFTER = "remove.after";
    public static final String EVENT_UPDATE_BEFORE = "update.before";
    public static final String EVENT_UPDATE_AFTER = "update.after";
    public static final String EVENT_READ_BEFORE = "read.before";
    public static final String EVENT_READ_AFTER = "read.after";
    public static final String EVENT_LINK_BEFORE = "link.before";
    public static final String EVENT_LINK_AFTER = "link.after";
    public static final String EVENT_UNLINK_BEFORE = "unlink.before";
    public static final String EVENT_UNLINK_AFTER = "unlink.after";
    public static final String EVENT_WIP_ADD_BEFORE = "wip.add.before";
    public static final String EVENT_WIP_ADD_AFTER = "wip.add.after";
    public static final String EVENT_FILE_ATTACH_BEFORE = "file.attach.before";
    public static final String EVENT_FILE_ATTACH_AFTER = "file.attach.after";
    public static final String EVENT_FILE_DETACH_BEFORE = "file.detach.before";
    public static final String EVENT_FILE_DETACH_AFTER = "file.detach.after";
    public static final String EVENT_FILE_CHECKIN_BEFORE = "file.checkin.before";
    public static final String EVENT_FILE_CHECKIN_AFTER = "file.checkin.after";
    public static final String EVENT_FILE_CHECKOUT_BEFORE = "file.checkout.before";
    public static final String EVENT_FILE_CHECKOUT_AFTER = "file.checkout.after";
    public static final String EVENT_MODEL_OPENED = "model.opened";
    public static final String EVENT_MODEL_CLOSED = "model.closed";
    public static final String EVENT_START_BEFORE = "start.before";
    public static final String EVENT_START_AFTER = "start.after";
    public static final String EVENT_FINISH_BEFORE = "finish.before";
    public static final String EVENT_FINISH_AFTER = "finish.after";
    public static final String SCRIPT_JAVASCRIPT = "javascript";
    public static final String SCRIPT_PYTHON = "python";
    public static final String SCRIPT_TCL = "tcl";
    public static final String SCRIPT_BEANSHELL = "java";
    public static final String wipVersionString = "wip";
}
