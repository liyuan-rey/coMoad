// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ClientUtil.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.util.Utils;
import java.io.File;
import java.io.PrintStream;
import javax.swing.ImageIcon;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD

public class ClientUtil
{
    class FileCallBack
        implements FileTransferCallback
    {

        public void transferBytes(int i)
        {
        }

        FileCallBack()
        {
        }
    }


    public ClientUtil()
    {
        fileCallback = new FileCallBack();
    }

    public static boolean cachingFile(String serverPath, String clientPath)
    {
        try
        {
            long serverFileSize = DynaMOAD.dss.getFileSize(serverPath);
            long serverFileLMD = DynaMOAD.dss.getLastModifiedDate(serverPath);
            File imageFile = new File(clientPath);
            long clientFileSize = imageFile.length();
            long clientFileLMD = imageFile.lastModified();
            return serverFileSize == clientFileSize && serverFileLMD == clientFileLMD;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return false;
    }

    public static ImageIcon getImageIcon(String ouid)
    {
        try
        {
            if(ouid != null)
            {
                DOSChangeable classInfo = new DOSChangeable();
                classInfo = DynaMOAD.dos.getClass(ouid);
                if(classInfo != null)
                {
                    ImageIcon imageIcon = null;
                    String fileNam = (String)classInfo.get("icon");
                    if(!Utils.isNullString(fileNam))
                    {
                        File imageFile = new File(System.getProperty("user.dir") + FS + "icons" + FS + fileNam);
                        if(!cachingFile("/icons/" + fileNam, System.getProperty("user.dir") + FS + "icons" + FS + fileNam))
                        {
                            imageFile.delete();
                            DynaMOAD.dss.downloadFile("/icons/" + fileNam, System.getProperty("user.dir") + FS + "icons" + FS + fileNam, null);
                        }
                        imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                        imageFile = null;
                        return imageIcon;
                    }
                } else
                {
                    ImageIcon imageIcon = null;
                    String fileNam = ouid + ".gif";
                    boolean result = false;
                    if(!Utils.isNullString(fileNam))
                    {
                        File imageFile = new File(System.getProperty("user.dir") + FS + "icons" + FS + fileNam);
                        if(!cachingFile("/icons/" + fileNam, System.getProperty("user.dir") + FS + "icons" + FS + fileNam))
                            result = DynaMOAD.dss.downloadFileForce("/icons/" + fileNam, System.getProperty("user.dir") + FS + "icons" + FS + fileNam, null);
                        if(result || imageFile.exists())
                            imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                        else
                            imageIcon = null;
                        imageFile = null;
                        return imageIcon;
                    }
                }
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return null;
    }

    private static FileCallBack fileCallback = null;
    private static String FS = System.getProperty("file.separator");

}