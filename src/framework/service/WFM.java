// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WFM.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import dyna.framework.service.dos.DOSChangeable;
import java.util.ArrayList;
import java.util.HashMap;

public interface WFM
    extends Service
{

    public abstract void ping();

    public abstract DOSChangeable getModelDefinition()
        throws IIPRequestException;

    public abstract ArrayList getCurrentProcesses()
        throws IIPRequestException;

    public abstract String createParticipantDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeParticipantDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getParticipantDefinition(String s)
        throws IIPRequestException;

    public abstract void setParticipantDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupParticipantByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupParticipantByName(String s)
        throws IIPRequestException;

    public abstract String createApplicationDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeApplicationDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getApplicationDefinition(String s)
        throws IIPRequestException;

    public abstract void setApplicationDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupApplicationByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupApplicationByName(String s)
        throws IIPRequestException;

    public abstract void setActivityApplication(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList getApplicationInvokeParameters(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList invokeApplication(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract String createProcedureDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeProcedureDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getProcedureDefinition(String s)
        throws IIPRequestException;

    public abstract void setProcedureDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupProcedureByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupProcedureByName(String s)
        throws IIPRequestException;

    public abstract void setProcedureInvokeParameters(String s, String s1, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList getProcedureInvokeParameters(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList invokeProcedure(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract String createDataDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeDataDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getDataDefinition(String s)
        throws IIPRequestException;

    public abstract void setDataDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupDataByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupDataByName(String s)
        throws IIPRequestException;

    public abstract ArrayList getObjectParameter(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList getObjectParameters(String s)
        throws IIPRequestException;

    public abstract ArrayList getExtendedAttribute(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList getExtendedAttributes(String s)
        throws IIPRequestException;

    public abstract void createSimulationDataDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeSimulationDataDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getSimulationDataDefinition(String s)
        throws IIPRequestException;

    public abstract void setSimulationDataDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract DOSChangeable getObjectSimulationData(String s)
        throws IIPRequestException;

    public abstract void setObjectSimulationData(String s)
        throws IIPRequestException;

    public abstract String createProcessDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeProcessDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getProcessDefinition(String s)
        throws IIPRequestException;

    public abstract void setProcessDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String createProcess(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeProcess(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getProcess(String s)
        throws IIPRequestException;

    public abstract void setProcess(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listProcessByObject(String s)
        throws IIPRequestException;

    public abstract String lookupProcessByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupProcessByName(String s)
        throws IIPRequestException;

    public abstract ArrayList getProcessActivities(String s)
        throws IIPRequestException;

    public abstract void startProcess(String s)
        throws IIPRequestException;

    public abstract void finishProcess(String s)
        throws IIPRequestException;

    public abstract void resumeProcess(String s)
        throws IIPRequestException;

    public abstract void refreshProcessStatus(String s)
        throws IIPRequestException;

    public abstract String getProcessStatus(String s)
        throws IIPRequestException;

    public abstract void setProcessStatus(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList getCurrentActivities(String s)
        throws IIPRequestException;

    public abstract ArrayList getCurrentActivitiesByPerfomer(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList getProcessesByType(String s)
        throws IIPRequestException;

    public abstract ArrayList getProcessActivities4Graph(String s)
        throws IIPRequestException;

    public abstract ArrayList getProcessTransitions4Graph(String s)
        throws IIPRequestException;

    public abstract String getDefinitionOfProcess(String s)
        throws IIPRequestException;

    public abstract String createActivityDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeActivityDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getActivityDefinition(String s)
        throws IIPRequestException;

    public abstract void setActivityDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String createActivity(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeActivity(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getActivity(String s)
        throws IIPRequestException;

    public abstract void setActivity(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupActivityByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupActivityByName(String s)
        throws IIPRequestException;

    public abstract void startActivity(String s)
        throws IIPRequestException;

    public abstract void finishActivity(String s, String s1)
        throws IIPRequestException;

    public abstract void finishActivity(String s)
        throws IIPRequestException;

    public abstract String getActivityStatus(String s)
        throws IIPRequestException;

    public abstract void setActivityStatus(String s, String s1)
        throws IIPRequestException;

    public abstract void markActivityForInboxControl(String s, String s1)
        throws IIPRequestException;

    public abstract void createMultipleActivities(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList getMultipleActivities(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getSubworkflow(String s)
        throws IIPRequestException;

    public abstract ArrayList getSplitTransitions(String s)
        throws IIPRequestException;

    public abstract ArrayList getJoinTransitions(String s)
        throws IIPRequestException;

    public abstract ArrayList getNextActivities(String s)
        throws IIPRequestException;

    public abstract boolean determineLoopCondition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getActivityApplication(String s)
        throws IIPRequestException;

    public abstract void setApplicationInvokeParameters(String s, String s1, ArrayList arraylist)
        throws IIPRequestException;

    public abstract void setActivityProcedure(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract DOSChangeable getActivityProcedure(String s)
        throws IIPRequestException;

    public abstract String lookupActivityByDataValue(String s, String s1)
        throws IIPRequestException;

    public abstract void setActivityDimension(String s, Integer integer, Integer integer1, Integer integer2, Integer integer3)
        throws IIPRequestException;

    public abstract String getDefinitionOfActivity(String s)
        throws IIPRequestException;

    public abstract String createTransition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeTransition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getTransition(String s)
        throws IIPRequestException;

    public abstract void setTransition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String lookupTransitionByIdentifier(String s)
        throws IIPRequestException;

    public abstract ArrayList lookupTransitionByName(String s)
        throws IIPRequestException;

    public abstract ArrayList getTrnasitionConditions(String s)
        throws IIPRequestException;

    public abstract void setTransitionConditions(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList determineTrnasitionConditions(String s)
        throws IIPRequestException;

    public abstract boolean determineTrnasitionCondition(String s)
        throws IIPRequestException;

    public abstract String createWorkItemDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeWorkItemDefinition(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getWorkItemDefinition(String s)
        throws IIPRequestException;

    public abstract ArrayList getWorkItemDefinitions(String s)
        throws IIPRequestException;

    public abstract void setWorkItemDefinition(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void createWorkItem(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeWorkItem(String s, String s1)
        throws IIPRequestException;

    public abstract DOSChangeable getWorkItem(String s, String s1)
        throws IIPRequestException;

    public abstract void setWorkItem(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList getWorkItems(String s)
        throws IIPRequestException;

    public abstract void startWorkItem(String s, String s1)
        throws IIPRequestException;

    public abstract void finishWorkItem(String s, String s1)
        throws IIPRequestException;

    public abstract String getWorkItemStatus(String s, String s1)
        throws IIPRequestException;

    public abstract void setWorkItemStatus(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract DOSChangeable lookupWorkItemByTargetObject(String s)
        throws IIPRequestException;

    public abstract void setResponsibles(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList getResponsibles(String s)
        throws IIPRequestException;

    public abstract void addChild(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract void removeChild(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract ArrayList getChildren(String s)
        throws IIPRequestException;

    public abstract String cloneProcessDefinition(String s, String s1)
        throws IIPRequestException;

    public abstract String cloneActivityDefinition(String s, String s1)
        throws IIPRequestException;

    public abstract String cloneWorkItemDefinition(String s, String s1)
        throws IIPRequestException;

    public abstract String cloneProcess(String s)
        throws IIPRequestException;

    public abstract String cloneActivity(String s, String s1)
        throws IIPRequestException;

    public abstract String cloneTransition(String s, String s1, String s2, String s3)
        throws IIPRequestException;

    public abstract void setSubProcess(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract DOSChangeable getSubProcess(String s)
        throws IIPRequestException;

    public abstract DOSChangeable getSuperActivity(String s)
        throws IIPRequestException;

    public abstract int countActivity(String s, String s1)
        throws IIPRequestException;

    public abstract void setActivityFinishTime(String s, String s1)
        throws IIPRequestException;

    public abstract boolean isValidProcess(String s)
        throws IIPRequestException;

    public abstract void addProcessDefinitionToModel(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList listProcessDefinitionOfModel(String s)
        throws IIPRequestException;

    public abstract void removeProcessDefinitionFromModel(String s, String s1)
        throws IIPRequestException;

    public abstract String getModelOfProcessDefinition(String s)
        throws IIPRequestException;

    public abstract void addClassToProcessDefinition(String s, String s1)
        throws IIPRequestException;

    public abstract void removeClassFromProcessDefinition(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList listClassOfProcessDefinition(String s)
        throws IIPRequestException;

    public abstract ArrayList listProcessDeifnitionOfClass(String s)
        throws IIPRequestException;

    public abstract ArrayList listInboxProcessByUser(String s)
        throws IIPRequestException;

    public abstract ArrayList listSentProcessByUser(String s)
        throws IIPRequestException;

    public abstract ArrayList listCompletedProcessByUser(String s)
        throws IIPRequestException;

    public abstract void createComments(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract String getProcessComments(String s)
        throws IIPRequestException;

    public abstract String isLocked(String s)
        throws IIPRequestException;

    public abstract boolean addActivityStatusMap(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeActivityStatusMap(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listActivityStatus(String s)
        throws IIPRequestException;

    public abstract ArrayList getListOfAllParticipantsOfProcess(String s)
        throws IIPRequestException;

    public abstract ArrayList getListOfAllReviewerOfProcess(String s)
        throws IIPRequestException;

    public abstract ArrayList getListOfAllApproverOfProcess(String s)
        throws IIPRequestException;

    public static final String TRANSITION_KIND_REGULAR = "N";
    public static final String TRANSITION_KIND_ACCEPT = "A";
    public static final String TRANSITION_KIND_REJECT = "R";
    public static final String TRANSITION_KIND_LOOP_BEGIN = "B";
    public static final String TRANSITION_KIND_LOOP_END = "E";
}
