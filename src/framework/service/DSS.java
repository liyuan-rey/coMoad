// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import dyna.framework.service.dss.FileTransferCallback;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public interface DSS
    extends Service
{

    public abstract void ping();

    public abstract boolean createStorage(HashMap hashmap)
        throws IIPRequestException;

    public abstract HashMap getStorage(String s)
        throws IIPRequestException;

    public abstract boolean setStorage(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeStorage(String s)
        throws IIPRequestException;

    public abstract boolean mountStorage(String s, String s1)
        throws IIPRequestException;

    public abstract boolean unmountStorage(String s)
        throws IIPRequestException;

    public abstract ArrayList listStorageId()
        throws IIPRequestException;

    public abstract ArrayList listMount()
        throws IIPRequestException;

    public abstract String getStorageIdOfMount(String s)
        throws IIPRequestException;

    public abstract boolean makeFolder(String s, String s1)
        throws IIPRequestException;

    public abstract boolean makeFolders(String s)
        throws IIPRequestException;

    public abstract boolean deleteFolder(String s)
        throws IIPRequestException;

    public abstract boolean renameFolder(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList list(String s)
        throws IIPRequestException;

    public abstract ArrayList listFolder(String s)
        throws IIPRequestException;

    public abstract ArrayList listFile(String s)
        throws IIPRequestException;

    public abstract boolean createEmptyFile(String s, String s1)
        throws IIPRequestException;

    public abstract boolean deleteFile(String s)
        throws IIPRequestException;

    public abstract boolean renameFile(String s, String s1)
        throws IIPRequestException;

    public abstract long getFileSize(String s)
        throws IIPRequestException;

    public abstract boolean copyFile(String s, String s1)
        throws IIPRequestException;

    public abstract String getFileTypeId(String s)
        throws IIPRequestException;

    public abstract String getMIMEType(String s)
        throws IIPRequestException;

    public abstract String getApplication(String s)
        throws IIPRequestException;

    public abstract String getExtension(String s)
        throws IIPRequestException;

    public abstract boolean createFileType(HashMap hashmap)
        throws IIPRequestException;

    public abstract HashMap getFileType(String s)
        throws IIPRequestException;

    public abstract boolean setFileType(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeFileType(String s)
        throws IIPRequestException;

    public abstract ArrayList listFileTypeId()
        throws IIPRequestException;

    public abstract String getName(String s)
        throws IIPRequestException;

    public abstract String getParent(String s)
        throws IIPRequestException;

    public abstract long getLastModifiedDate(String s)
        throws IIPRequestException;

    public abstract boolean setLastModifiedDate(String s, Long long1)
        throws IIPRequestException;

    public abstract boolean isFile(String s)
        throws IIPRequestException;

    public abstract boolean isFolder(String s)
        throws IIPRequestException;

    public abstract boolean isHidden(String s)
        throws IIPRequestException;

    public abstract boolean exists(String s)
        throws IIPRequestException;

    public abstract boolean canWrite(String s)
        throws IIPRequestException;

    public abstract String getStorageId(String s)
        throws IIPRequestException;

    public abstract boolean doFileConversion(String s, ArrayList arraylist, String s1, String s2)
        throws IIPRequestException;

    public abstract boolean remoteCheckErrFile(String s, ArrayList arraylist, String s1)
        throws IIPRequestException;

    public abstract int prepareInputStream(String s)
        throws IIPRequestException;

    public abstract int prepareOutputStream(String s, Long long1)
        throws IIPRequestException;

    public abstract InputStream getInputStream(String s)
        throws IIPRequestException;

    public abstract OutputStream getOutputStream(String s)
        throws IIPRequestException;

    public abstract OutputStream getOutputStream(String s, Long long1)
        throws IIPRequestException;

    public abstract boolean downloadFile(String s, String s1, FileTransferCallback filetransfercallback)
        throws IIPRequestException;

    public abstract boolean uploadFile(String s, String s1, FileTransferCallback filetransfercallback)
        throws IIPRequestException;

    public abstract boolean downloadFileForce(String s, String s1, FileTransferCallback filetransfercallback)
        throws IIPRequestException;

    public abstract boolean uploadFileForce(String s, String s1, FileTransferCallback filetransfercallback)
        throws IIPRequestException;

    public abstract String executeServerScript(String s, String s1)
        throws IIPRequestException;

    public abstract String executeServerScript(String s)
        throws IIPRequestException;

    public abstract int executeServerExternalProgram(String s, ArrayList arraylist)
        throws IIPRequestException;

    public abstract int executeServerExternalProgram(String s)
        throws IIPRequestException;
}
