// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dss.FileConveter;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.framework.service.dss.RemoteFileConveter;
import dyna.util.Utils;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package dyna.framework.service:
//            DSS, NDS, AUS

public class DSSImpl extends ServiceServer
    implements DSS
{
    private class PrepareDownload extends Thread
    {

        public int getPort()
        {
            return port;
        }

        public void run()
        {
            getClass();
            byte buffer[] = new byte[8192];
            int inBytes = 0;
            boolean finished = false;
            try
            {
                client = server.accept();
                server.close();
                server = null;
                getClass();
                out = new BufferedOutputStream(client.getOutputStream(), 8192);
                do
                {
                    getClass();
                    inBytes = in.read(buffer, 0, 8192);
                    if(inBytes > 0)
                        out.write(buffer, 0, inBytes);
                    else
                        finished = true;
                } while(!finished);
                out.flush();
                out.close();
                out = null;
                FileDescriptor fd = fis.getFD();
                fd.sync();
                fd = null;
                in.close();
                in = null;
                client.close();
                client = null;
            }
            catch(IOException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception) { }
                    in = null;
                }
                if(fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Exception exception1) { }
                    fis = null;
                }
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception2) { }
                    out = null;
                }
                if(client != null)
                {
                    try
                    {
                        client.close();
                    }
                    catch(Exception exception3) { }
                    client = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception4) { }
                    server = null;
                }
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    tempFile.delete();
                }
                catch(Exception exception6) { }
                file = null;
                tempFile = null;
            }
            return;
        }

        private BufferedInputStream in;
        private BufferedOutputStream out;
        private FileInputStream fis;
        private int port;
        private ServerSocket server;
        private Socket client;
        private File tempFile;
        private File file;


        public PrepareDownload(File file, File tempFile)
            throws IIPRequestException
        {
            port = 0;
            server = null;
            client = null;
            this.tempFile = null;
            this.file = null;
            try
            {
                fis = new FileInputStream(file);
                getClass();
                in = new BufferedInputStream(fis, 8192);
                server = new ServerSocket(0);
                server.setSoTimeout(30000);
                port = server.getLocalPort();
                this.tempFile = tempFile;
                this.file = file;
            }
            catch(ConnectException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception) { }
                    in = null;
                }
                if(fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Exception exception1) { }
                    fis = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception2) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(BindException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception3) { }
                    in = null;
                }
                if(fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Exception exception4) { }
                    fis = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception5) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(SocketException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception6) { }
                    in = null;
                }
                if(fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Exception exception7) { }
                    fis = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception8) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(FileNotFoundException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception9) { }
                    in = null;
                }
                if(fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Exception exception10) { }
                    fis = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception11) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(IOException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception12) { }
                    in = null;
                }
                if(fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Exception exception13) { }
                    fis = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception14) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
        }
    }

    private class PrepareUpload extends Thread
    {

        public int getPort()
        {
            return port;
        }

        public void run()
        {
            if(server == null)
                return;
            getClass();
            byte buffer[] = new byte[8192];
            int inBytes = 0;
            boolean finished = false;
            try
            {
                client = server.accept();
                server.close();
                server = null;
                getClass();
                in = new BufferedInputStream(client.getInputStream(), 8192);
                do
                {
                    getClass();
                    inBytes = in.read(buffer, 0, 8192);
                    if(inBytes > 0)
                        out.write(buffer, 0, inBytes);
                    else
                        finished = true;
                } while(!finished);
                out.flush();
                fos.flush();
                FileDescriptor fd = fos.getFD();
                fd.sync();
                fd = null;
                out.close();
                out = null;
                fos.close();
                fos = null;
                in.close();
                in = null;
                client.close();
                client = null;
                uploadSyncControl.put(path, Boolean.TRUE);
            }
            catch(IOException e)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception exception) { }
                    in = null;
                }
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception1) { }
                    out = null;
                }
                if(fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch(Exception exception2) { }
                    fos = null;
                }
                if(client != null)
                {
                    try
                    {
                        client.close();
                    }
                    catch(Exception exception3) { }
                    client = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception4) { }
                    server = null;
                }
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    file.delete();
                    if(date != null)
                        tempFile.setLastModified(date.longValue());
                    tempFile.renameTo(file);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                file = null;
                tempFile = null;
            }
            return;
        }

        private BufferedInputStream in;
        private BufferedOutputStream out;
        private FileOutputStream fos;
        private int port;
        private ServerSocket server;
        private Socket client;
        private File tempFile;
        private File file;
        private String path;
        public Long date;


        public PrepareUpload(File file, File tempFile, String path)
            throws IIPRequestException
        {
            port = 0;
            server = null;
            client = null;
            this.tempFile = null;
            this.file = null;
            this.path = null;
            date = null;
            try
            {
                fos = new FileOutputStream(tempFile);
                getClass();
                out = new BufferedOutputStream(fos, 8192);
                server = new ServerSocket(0);
                server.setSoTimeout(30000);
                port = server.getLocalPort();
                this.tempFile = tempFile;
                this.file = file;
                this.path = path;
            }
            catch(ConnectException e)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception) { }
                    out = null;
                }
                if(fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch(Exception exception1) { }
                    fos = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception2) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(BindException e)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception3) { }
                    out = null;
                }
                if(fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch(Exception exception4) { }
                    fos = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception5) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(SocketException e)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception6) { }
                    out = null;
                }
                if(fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch(Exception exception7) { }
                    fos = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception8) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(FileNotFoundException e)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception9) { }
                    out = null;
                }
                if(fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch(Exception exception10) { }
                    fos = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception11) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
            catch(IOException e)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Exception exception12) { }
                    out = null;
                }
                if(fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch(Exception exception13) { }
                    fos = null;
                }
                if(server != null)
                {
                    try
                    {
                        server.close();
                    }
                    catch(Exception exception14) { }
                    server = null;
                }
                port = 0;
                this.tempFile = null;
                this.file = null;
                e.printStackTrace();
                throw new IIPRequestException(e.fillInStackTrace().toString());
            }
        }
    }


    public DSSImpl()
    {
        nds = null;
        aus = null;
        uploadSyncControl = new HashMap();
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            nds = (NDS)getServiceInstance("DF30NDS1");
            aus = (AUS)getServiceInstance("DF30AUS1");
            getClass();
            if(nds.getValue("::/DSS_SYSTEM_DIRECTORY") == null)
            {
                nds.addNode("::", "DSS_SYSTEM_DIRECTORY", "DSS", "");
                getClass();
                nds.addNode("::/DSS_SYSTEM_DIRECTORY", "STORAGE", "DSS", "STORAGE");
                getClass();
                nds.addNode("::/DSS_SYSTEM_DIRECTORY", "FILETYPE", "DSS", "FILETYPE");
                getClass();
                nds.addNode("::/DSS_SYSTEM_DIRECTORY", "MOUNT", "DSS", "MOUNT");
            }
            HashMap filetype = new HashMap();
            filetype.put("filetype.id", "acad");
            filetype.put("extension", "dwg");
            filetype.put("description", "AutoCAD Drawing");
            filetype.put("mime.type", "model/acad");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "cadra");
            filetype.put("extension", "cad");
            filetype.put("description", "CADRA Drawing");
            filetype.put("mime.type", "model/cadra");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "dxf");
            filetype.put("extension", "dxf");
            filetype.put("description", "Drawing Exchange Format");
            filetype.put("mime.type", "model/dxf");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "i-deas");
            filetype.put("extension", "mod");
            filetype.put("description", "I-DEAS Master Series");
            filetype.put("mime.type", "model/i-deas");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "ms-word");
            filetype.put("extension", "doc");
            filetype.put("description", "Microsoft Word");
            filetype.put("mime.type", "application/msword");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "acrobat");
            filetype.put("extension", "pdf");
            filetype.put("description", "Adobe Acrobat");
            filetype.put("mime.type", "application/pdf");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "postscript");
            filetype.put("extension", "ps");
            filetype.put("description", "PostScript File");
            filetype.put("mime.type", "application/postscript");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "csv");
            filetype.put("extension", "csv");
            filetype.put("description", "Comma Separated Value File");
            filetype.put("mime.type", "application/csv");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "ms-excel");
            filetype.put("extension", "xls");
            filetype.put("description", "Microsoft Excel");
            filetype.put("mime.type", "application/vnd.ms-excel");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "ms-powerpoint");
            filetype.put("extension", "ppt");
            filetype.put("description", "Microsoft PowerPoint");
            filetype.put("mime.type", "application/vnd.ms-powerpoint");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "java");
            filetype.put("extension", "java");
            filetype.put("description", "Java Source");
            filetype.put("mime.type", "application/x-java");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "javascript");
            filetype.put("extension", "js");
            filetype.put("description", "Java Script");
            filetype.put("mime.type", "application/x-javascript");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "javaclass");
            filetype.put("extension", "class");
            filetype.put("description", "Java Bytecode");
            filetype.put("mime.type", "application/x-java-vm");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "jar");
            filetype.put("extension", "jar");
            filetype.put("description", "Java Archive");
            filetype.put("mime.type", "application/x-java-archive");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "python");
            filetype.put("extension", "py");
            filetype.put("description", "Python Script");
            filetype.put("mime.type", "application/x-python");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "shellscript");
            filetype.put("extension", "sh");
            filetype.put("description", "Shell Script");
            filetype.put("mime.type", "application/x-sh");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "flash");
            filetype.put("extension", "swf");
            filetype.put("description", "Shockwave Flash");
            filetype.put("mime.type", "application/x-shockwave-flash");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "tar");
            filetype.put("extension", "tar");
            filetype.put("description", "Tar Archiving Format");
            filetype.put("mime.type", "application/x-tar");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "tcl");
            filetype.put("extension", "tcl");
            filetype.put("description", "TCL Script");
            filetype.put("mime.type", "application/x-tcl");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "zip");
            filetype.put("extension", "zip");
            filetype.put("description", "Zip Archive");
            filetype.put("mime.type", "application/zip");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "mp3");
            filetype.put("extension", "mp3");
            filetype.put("description", "MPEG Audio Layer-3");
            filetype.put("mime.type", "audio/mpeg");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "midi");
            filetype.put("extension", "mid");
            filetype.put("description", "Musical Instrument Digital Interface");
            filetype.put("mime.type", "audio/x-midi");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "realaudio");
            filetype.put("extension", "ra");
            filetype.put("description", "Real Auidio");
            filetype.put("mime.type", "audio/x-realaudio");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "wave");
            filetype.put("extension", "wav");
            filetype.put("description", "Wave Audio");
            filetype.put("mime.type", "audio/x-wav");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "bmp");
            filetype.put("extension", "bmp");
            filetype.put("description", "Bitmap Raster Image");
            filetype.put("mime.type", "image/bmp");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "cgm");
            filetype.put("extension", "cgm");
            filetype.put("description", "Computer Graphics Metafile");
            filetype.put("mime.type", "image/cgm");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "gif");
            filetype.put("extension", "gif");
            filetype.put("description", "GIF Image");
            filetype.put("mime.type", "image/gif");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "jpeg");
            filetype.put("extension", "jpg");
            filetype.put("description", "JPEG Image");
            filetype.put("mime.type", "image/jpeg");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "tiff");
            filetype.put("extension", "tif");
            filetype.put("description", "Tag Image File Format");
            filetype.put("mime.type", "image/tiff");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "iges");
            filetype.put("extension", "igs");
            filetype.put("description", "International Graphic Exchange Standard");
            filetype.put("mime.type", "model/iges");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "vrml");
            filetype.put("extension", "wrl");
            filetype.put("description", "Virtual Reality Modeling Language");
            filetype.put("mime.type", "model/vrml");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "text");
            filetype.put("extension", "txt");
            filetype.put("description", "Plain Text");
            filetype.put("mime.type", "text/plain");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "rtf");
            filetype.put("extension", "rtf");
            filetype.put("description", "Rich Text Format");
            filetype.put("mime.type", "text/rtf");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "html");
            filetype.put("extension", "html");
            filetype.put("description", "Hyper-text Mark-up Langauge");
            filetype.put("mime.type", "text/html");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "xml");
            filetype.put("extension", "xml");
            filetype.put("description", "Extensible Mark-up Language");
            filetype.put("mime.type", "text/xml");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "css");
            filetype.put("extension", "css");
            filetype.put("description", "Cascading Style Sheets");
            filetype.put("mime.type", "text/css");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "dtd");
            filetype.put("extension", "dtd");
            filetype.put("description", "Document Type Definition");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "wml");
            filetype.put("extension", "wml");
            filetype.put("description", "Wireless Mark-up Language");
            filetype.put("mime.type", "text/vnd.wap.wml");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "sgml");
            filetype.put("extension", "sgml");
            filetype.put("description", "Standard Generalized Mark-up Language");
            filetype.put("mime.type", "text/sgml");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "mpeg");
            filetype.put("extension", "mpg");
            filetype.put("description", "MPEG Compressed Video");
            filetype.put("mime.type", "video/mpeg");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "quicktime");
            filetype.put("extension", "mov");
            filetype.put("description", "Apple QuickTime Video");
            filetype.put("mime.type", "video/quicktime");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "avi");
            filetype.put("extension", "avi");
            filetype.put("description", "Microsoft Video");
            filetype.put("mime.type", "video/x-msvideo");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "catia4-model");
            filetype.put("extension", "model");
            filetype.put("description", "CATIA V4 Model");
            filetype.put("mime.type", "model/catia4-model");
            filetype.put("converted.filetypeid", "vrml^hpgl2");
            filetype.put("converter", "CATIAV4RemoteFileConverter");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "catia4-session");
            filetype.put("extension", "session");
            filetype.put("description", "CATIA V4 Session");
            filetype.put("mime.type", "model/catia4-session");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "hwp");
            filetype.put("extension", "hwp");
            filetype.put("description", "Hangul Word");
            filetype.put("mime.type", "application/hwp");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "sdsword");
            filetype.put("extension", "gul");
            filetype.put("description", "SDS Word");
            filetype.put("mime.type", "application/gul");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "proe-part");
            filetype.put("extension", "prt");
            filetype.put("description", "ProEngineer Part");
            filetype.put("mime.type", "model/proe-part");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "proe-drawing");
            filetype.put("extension", "drw");
            filetype.put("description", "ProEngineer Drawing");
            filetype.put("mime.type", "model/proe-drawing");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "proe-assembly");
            filetype.put("extension", "asm");
            filetype.put("description", "ProEngineer Assembly");
            filetype.put("mime.type", "model/proe-assembly");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "envision");
            filetype.put("extension", "env");
            filetype.put("description", "Envision");
            filetype.put("mime.type", "model/envision");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "ug-part");
            filetype.put("extension", "prt");
            filetype.put("description", "UniGraphics Part");
            filetype.put("mime.type", "model/ug-part");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "se-part");
            filetype.put("extension", "par");
            filetype.put("description", "SolidEdge Part");
            filetype.put("mime.type", "model/se-part");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "se-draft");
            filetype.put("extension", "dft");
            filetype.put("description", "SolidEdge Draft");
            filetype.put("mime.type", "model/se-draft");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "se-assembly");
            filetype.put("extension", "asm");
            filetype.put("description", "SolidEdge Assembly");
            filetype.put("mime.type", "model/se-assembly");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "sw-part");
            filetype.put("extension", "sldprt");
            filetype.put("description", "SolidWorks Part");
            filetype.put("mime.type", "model/sw-part");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "sw-drawing");
            filetype.put("extension", "slddrw");
            filetype.put("description", "SolidWorks Drawing");
            filetype.put("mime.type", "model/sw-drawing");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "sw-assembly");
            filetype.put("extension", "sldasm");
            filetype.put("description", "SolidWorks Assembly");
            filetype.put("mime.type", "model/sw-assembly");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "reportdesigner");
            filetype.put("extension", "mrd");
            filetype.put("description", "Report Designer");
            filetype.put("mime.type", "application/reportdesigner");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "catia5-product");
            filetype.put("extension", "CATProduct");
            filetype.put("description", "CATIA V5 Product");
            filetype.put("mime.type", "model/catia5-product");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "catia5-part");
            filetype.put("extension", "CATPart");
            filetype.put("description", "CATIA V5 Part");
            filetype.put("mime.type", "model/catia5-part");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "catia5-drawing");
            filetype.put("extension", "CATDrawing");
            filetype.put("description", "CATIA V5 Drawing");
            filetype.put("mime.type", "model/catia5-drawing");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "hpgl2");
            filetype.put("extension", "gl2");
            filetype.put("description", "HP-GL 2");
            filetype.put("mime.type", "application/hpgl2");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "inventor6-product");
            filetype.put("extension", "iam");
            filetype.put("description", "Inventor V6 Product");
            filetype.put("mime.type", "model/inventor6-product");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "inventor6-part");
            filetype.put("extension", "ipt");
            filetype.put("description", "Inventor V6 Part");
            filetype.put("mime.type", "model/inventor6-part");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "inventor6-drawing");
            filetype.put("extension", "idw");
            filetype.put("description", "Inventor V6 Drawing");
            filetype.put("mime.type", "model/inventor6-drawing");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "cadam-drafting");
            filetype.put("extension", "cdd");
            filetype.put("description", "CADAM Drafting");
            filetype.put("mime.type", "drawing/cadam");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "cadam-overlay");
            filetype.put("extension", "cdo");
            filetype.put("description", "CADAM Overlay");
            filetype.put("mime.type", "drawing/cadam-overlay");
            createFileType(filetype);
            filetype.clear();
            filetype.put("filetype.id", "zuocad");
            filetype.put("extension", "zdw");
            filetype.put("description", "Zuo CAD Drawing");
            filetype.put("mime.type", "model/zuocad");
            createFileType(filetype);
            filetype.clear();
            filetype = null;
            HashMap storage = new HashMap();
            storage.put("storage.id", "storage.default");
            storage.put("broker.id", "BRKRdefault");
            storage.put("base.directory.path", System.getProperty("user.dir") + fs + "dssroot");
            storage.put("description", "System default storage");
            if(createStorage(storage))
                mountStorage("storage.default", "/");
            storage.clear();
            storage = null;
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public boolean createStorage(HashMap storage)
        throws IIPRequestException
    {
        if(storage == null || storage.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String storageId = null;
        String tempString = null;
        String tempString2 = null;
        storageId = (String)storage.get("storage.id");
        if(Utils.isNullString(storageId))
            throw new IIPRequestException("Miss out mandatory field: storage.id");
        getClass();
        tempString2 = "::/DSS_SYSTEM_DIRECTORY/STORAGE" + "/" + storageId;
        tempString = nds.getValue(tempString2);
        if(!Utils.isNullString(tempString))
            return false;
        getClass();
        nds.addNode("::/DSS_SYSTEM_DIRECTORY/STORAGE", storageId, "DSS.STORAGE", storageId);
        tempString = (String)storage.get("broker.id");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "broker.id", tempString);
        tempString = (String)storage.get("base.directory.path");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "base.directory.path", tempString);
        tempString = (String)storage.get("description");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "description", tempString);
        tempString = sdf2.format(new Date());
        nds.addArgument(tempString2, "cdate", tempString);
        nds.addArgument(tempString2, "lmdate", tempString);
        return true;
    }

    public HashMap getStorage(String storageId)
        throws IIPRequestException
    {
        if(Utils.isNullString(storageId))
            throw new IIPRequestException("Miss out mandatory parameter: storageId");
        String tempString = null;
        getClass();
        tempString = "::/DSS_SYSTEM_DIRECTORY/STORAGE" + "/" + storageId;
        if(nds.getValue(tempString) == null)
            return null;
        else
            return nds.getArguments(tempString);
    }

    public boolean setStorage(HashMap storage)
        throws IIPRequestException
    {
        if(storage == null || storage.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        tempString = (String)storage.get("storage.id");
        if(Utils.isNullString(tempString))
            throw new IIPRequestException("Miss out mandatory parameter: storage.id");
        getClass();
        tempString2 = "::/DSS_SYSTEM_DIRECTORY/STORAGE" + "/" + tempString;
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("Storage not exists: " + tempString);
        tempString = (String)storage.get("broker.id");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "broker.id") == null)
                nds.addArgument(tempString2, "broker.id", tempString);
            else
                nds.setArgument(tempString2, "broker.id", tempString);
            nds.setValue(tempString2, tempString);
        } else
        {
            nds.removeArgument(tempString2, "broker.id");
        }
        tempString = (String)storage.get("base.directory.path");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "base.directory.path") == null)
                nds.addArgument(tempString2, "base.directory.path", tempString);
            else
                nds.setArgument(tempString2, "base.directory.path", tempString);
            nds.setValue(tempString2, tempString);
        } else
        {
            nds.removeArgument(tempString2, "base.directory.path");
        }
        tempString = (String)storage.get("description");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "description") == null)
                nds.addArgument(tempString2, "description", tempString);
            else
                nds.setArgument(tempString2, "description", tempString);
            nds.setValue(tempString2, tempString);
        } else
        {
            nds.removeArgument(tempString2, "description");
        }
        tempString = sdf2.format(new Date());
        if(nds.getArgument(tempString2, "mdate") == null)
            nds.addArgument(tempString2, "mdate", tempString);
        else
            nds.setArgument(tempString2, "mdate", tempString);
        storage = null;
        return true;
    }

    public boolean removeStorage(String sotrageId)
        throws IIPRequestException
    {
        String tempString = null;
        if(Utils.isNullString(sotrageId))
            throw new IIPRequestException("Miss out mandatory parameter: sotrageId");
        getClass();
        tempString = "::/DSS_SYSTEM_DIRECTORY/STORAGE" + "/" + sotrageId;
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            throw new IIPRequestException("Storage not exsits: " + sotrageId);
        } else
        {
            nds.removeNode(tempString);
            return true;
        }
    }

    public boolean mountStorage(String storageId, String path)
        throws IIPRequestException
    {
        getClass();
        return nds.addNode("::/DSS_SYSTEM_DIRECTORY/MOUNT", path.replace('/', '^'), "DSS.MOUNT", storageId);
    }

    public boolean unmountStorage(String path)
        throws IIPRequestException
    {
        getClass();
        return nds.removeNode("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/" + path.replace('/', '^'));
    }

    public ArrayList listStorageId()
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/DSS_SYSTEM_DIRECTORY/STORAGE");
    }

    public ArrayList listMount()
        throws IIPRequestException
    {
        getClass();
        ArrayList mountList = nds.getChildNodeList("::/DSS_SYSTEM_DIRECTORY/MOUNT");
        ArrayList returnList = new ArrayList();
        for(int i = 0; i < mountList.size(); i++)
        {
            String mountPoint = (String)mountList.get(i);
            returnList.add(mountPoint.replace('^', '/'));
        }

        return returnList;
    }

    public String getStorageIdOfMount(String dsspath)
        throws IIPRequestException
    {
        if(Utils.isNullString(dsspath))
        {
            throw new IIPRequestException("Miss out mandatory parameter: dsspath");
        } else
        {
            String tempString = null;
            getClass();
            tempString = "::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/" + dsspath.replace('/', '^');
            return nds.getValue(tempString);
        }
    }

    public boolean makeFolder(String path, String name)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath + fs + name);
        if(!baseFile.getParentFile().isDirectory())
            return false;
        else
            return baseFile.mkdir();
    }

    public boolean makeFolders(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
        {
            return false;
        } else
        {
            File baseFile = new File(localPath);
            return baseFile.mkdirs();
        }
    }

    public boolean deleteFolder(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath);
        if(!baseFile.isDirectory())
            return false;
        else
            return baseFile.delete();
    }

    public boolean renameFolder(String path, String newName)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath);
        if(!baseFile.isDirectory())
            return false;
        else
            return baseFile.renameTo(new File(baseFile.getParent() + fs + newName));
    }

    public ArrayList list(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return null;
        File baseFile = new File(localPath);
        if(!baseFile.isDirectory())
            return null;
        ArrayList arrayList = null;
        String files[] = baseFile.list();
        if(files != null && files.length > 0)
        {
            arrayList = new ArrayList(files.length);
            for(int i = 0; i < files.length; i++)
                arrayList.add(files[i]);

        }
        files = (String[])null;
        baseFile = null;
        return arrayList;
    }

    public ArrayList listFolder(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return null;
        File baseFile = new File(localPath);
        if(!baseFile.isDirectory())
            return null;
        ArrayList arrayList = null;
        File files[] = baseFile.listFiles();
        if(files != null && files.length > 0)
        {
            arrayList = new ArrayList(files.length);
            for(int i = 0; i < files.length; i++)
                if(files[i].isDirectory())
                    arrayList.add(files[i].getName());

            if(arrayList.size() == 0)
                arrayList = null;
        }
        files = (File[])null;
        baseFile = null;
        return arrayList;
    }

    public ArrayList listFile(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return null;
        File baseFile = new File(localPath);
        if(!baseFile.isDirectory())
            return null;
        ArrayList arrayList = null;
        File files[] = baseFile.listFiles();
        if(files != null && files.length > 0)
        {
            arrayList = new ArrayList(files.length);
            for(int i = 0; i < files.length; i++)
                if(files[i].isFile())
                    arrayList.add(files[i].getName());

            if(arrayList.size() == 0)
                arrayList = null;
        }
        files = (File[])null;
        baseFile = null;
        return arrayList;
    }

    public boolean createEmptyFile(String path, String name)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath + fs + name);
        if(baseFile.exists())
            return false;
        boolean returnValue = false;
        try
        {
            returnValue = baseFile.createNewFile();
        }
        catch(IOException e)
        {
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        return returnValue;
    }

    public boolean deleteFile(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath);
        File tempFile = null;
        boolean result = false;
        try
        {
            result = baseFile.isFile();
            for(int i = 0; i < 5; i++)
            {
                if(result)
                    break;
                Runtime.getRuntime().wait(1000L);
                result = baseFile.isFile();
            }

            if(!result)
            {
                baseFile = null;
                return false;
            }
            result = baseFile.delete();
            for(int i = 0; i < 5; i++)
            {
                if(!baseFile.exists())
                    break;
                Runtime.getRuntime().wait(1000L);
                result = baseFile.delete();
            }

            return result;
        }
        catch(Exception exception)
        {
            baseFile = null;
        }
        tempFile = null;
        return result;
    }

    public boolean renameFile(String path, String newName)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath);
        if(!baseFile.isFile())
            return false;
        boolean result = false;
        try
        {
            result = baseFile.renameTo(new File(baseFile.getParent() + fs + newName));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        baseFile = null;
        return result;
    }

    public long getFileSize(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return -1L;
        File baseFile = new File(localPath);
        if(!baseFile.isFile())
        {
            baseFile = null;
            return -1L;
        }
        long result = -1L;
        try
        {
            result = baseFile.length();
        }
        catch(Exception exception) { }
        baseFile = null;
        return result;
    }

    public boolean copyFile(String path, String newPath)
        throws IIPRequestException
    {
        File baseFile1;
        File baseFile2;
        BufferedInputStream in;
        BufferedOutputStream out;
        String localPath1 = getLocalPath(path);
        if(Utils.isNullString(localPath1))
            return false;
        String localPath2 = getLocalPath(newPath);
        if(Utils.isNullString(localPath2))
            return false;
        baseFile1 = new File(localPath1);
        if(!baseFile1.isFile() || !baseFile1.canRead())
        {
            baseFile1 = null;
            return false;
        }
        baseFile2 = new File(localPath2);
        if(baseFile2.isDirectory())
        {
            baseFile1 = null;
            baseFile2 = null;
            return false;
        }
        byte buffer[] = (byte[])null;
        in = null;
        out = null;
        if(!baseFile2.exists())
            baseFile2.createNewFile();
        if(!baseFile2.canWrite())
            return false;
        getClass();
        byte buffer[] = new byte[8192];
        int inBytes = 0;
        boolean finished = false;
        FileInputStream fis = new FileInputStream(baseFile1);
        getClass();
        in = new BufferedInputStream(fis, 8192);
        FileOutputStream fos = new FileOutputStream(baseFile2);
        getClass();
        out = new BufferedOutputStream(fos, 8192);
        do
        {
            getClass();
            inBytes = in.read(buffer, 0, 8192);
            if(inBytes > 0)
                out.write(buffer, 0, inBytes);
            else
                finished = true;
        } while(!finished);
        FileDescriptor fdi = fis.getFD();
        fdi.sync();
        fdi = null;
        in.close();
        in = null;
        fis.close();
        fis = null;
        out.flush();
        fos.flush();
        FileDescriptor fdo = fos.getFD();
        fdo.sync();
        fdo = null;
        out.close();
        out = null;
        fos.close();
        fos = null;
        buffer = (byte[])null;
          goto _L1
        IOException e;
        e;
        byte buffer[] = (byte[])null;
        if(in != null)
        {
            try
            {
                in.close();
            }
            catch(IOException ioexception) { }
            in = null;
        }
        if(out != null)
        {
            try
            {
                out.close();
            }
            catch(IOException ioexception1) { }
            out = null;
        }
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        local;
        baseFile1 = null;
        baseFile2 = null;
        JVM INSTR ret 16;
_L1:
        return true;
    }

    public String getFileTypeId(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        File baseFile = new File(path);
        String fileName = baseFile.getName();
        int index = fileName.lastIndexOf('.');
        if(index < 0)
            return null;
        index++;
        getClass();
        String fileTypeId = nds.findKeyValue("::/DSS_SYSTEM_DIRECTORY/FILETYPE", fileName.substring(index));
        if(Utils.isNullString(fileTypeId))
            return null;
        index = fileTypeId.lastIndexOf('/');
        if(index < 0)
        {
            return null;
        } else
        {
            index++;
            return fileTypeId.substring(index).toLowerCase();
        }
    }

    public String getMIMEType(String fileTypeId)
        throws IIPRequestException
    {
        if(Utils.isNullString(fileTypeId))
            throw new IIPRequestException("Miss out mandatory parameter: fileTypeId");
        getClass();
        String tempString = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + fileTypeId;
        if(nds.getValue(tempString) == null)
            return null;
        else
            return nds.getArgument(tempString, "mime.type");
    }

    public String getApplication(String fileTypeId)
        throws IIPRequestException
    {
        if(Utils.isNullString(fileTypeId))
            throw new IIPRequestException("Miss out mandatory parameter: fileTypeId");
        getClass();
        String tempString = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + fileTypeId;
        if(nds.getValue(tempString) == null)
            return null;
        else
            return nds.getArgument(tempString, "application");
    }

    public String getExtension(String fileTypeId)
        throws IIPRequestException
    {
        if(Utils.isNullString(fileTypeId))
            throw new IIPRequestException("Miss out mandatory parameter: fileTypeId");
        getClass();
        String tempString = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + fileTypeId;
        if(nds.getValue(tempString) == null)
            return null;
        else
            return nds.getArgument(tempString, "extension");
    }

    public boolean createFileType(HashMap fileType)
        throws IIPRequestException
    {
        if(fileType == null || fileType.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String filetypeId = null;
        String extension = null;
        String tempString = null;
        String tempString2 = null;
        filetypeId = (String)fileType.get("filetype.id");
        if(Utils.isNullString(filetypeId))
            throw new IIPRequestException("Miss out mandatory field: filetype.id");
        getClass();
        tempString2 = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + filetypeId;
        tempString = nds.getValue(tempString2);
        if(!Utils.isNullString(tempString))
            return false;
        extension = (String)fileType.get("extension");
        getClass();
        nds.addNode("::/DSS_SYSTEM_DIRECTORY/FILETYPE", filetypeId, "DSS.FILETYPE", extension);
        if(!Utils.isNullString(extension))
            nds.addArgument(tempString2, "extension", extension);
        tempString = (String)fileType.get("application");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "application", tempString);
        tempString = (String)fileType.get("description");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "description", tempString);
        tempString = (String)fileType.get("mime.type");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "mime.type", tempString);
        tempString = (String)fileType.get("converted.filetypeid");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "converted.filetypeid", tempString);
        tempString = (String)fileType.get("converter");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "converter", tempString);
        return true;
    }

    public HashMap getFileType(String fileTypeId)
        throws IIPRequestException
    {
        if(Utils.isNullString(fileTypeId))
            throw new IIPRequestException("Miss out mandatory parameter: fileTypeId");
        String tempString = null;
        getClass();
        tempString = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + fileTypeId;
        if(nds.getValue(tempString) == null)
            return null;
        else
            return nds.getArguments(tempString);
    }

    public boolean setFileType(HashMap fileType)
        throws IIPRequestException
    {
        if(fileType == null || fileType.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        tempString = (String)fileType.get("filetype.id");
        if(Utils.isNullString(tempString))
            throw new IIPRequestException("Miss out mandatory parameter: filetype.id");
        getClass();
        tempString2 = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + tempString;
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("File type not exists: " + tempString);
        tempString = (String)fileType.get("extension");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "extension") == null)
                nds.addArgument(tempString2, "extension", tempString);
            else
                nds.setArgument(tempString2, "extension", tempString);
            nds.setValue(tempString2, tempString);
        } else
        {
            nds.removeArgument(tempString2, "extension");
        }
        tempString = (String)fileType.get("application");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "application") == null)
                nds.addArgument(tempString2, "application", tempString);
            else
                nds.setArgument(tempString2, "application", tempString);
        } else
        {
            nds.removeArgument(tempString2, "application");
        }
        tempString = (String)fileType.get("description");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "description") == null)
                nds.addArgument(tempString2, "description", tempString);
            else
                nds.setArgument(tempString2, "description", tempString);
        } else
        {
            nds.removeArgument(tempString2, "description");
        }
        tempString = (String)fileType.get("mime.type");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "mime.type") == null)
                nds.addArgument(tempString2, "mime.type", tempString);
            else
                nds.setArgument(tempString2, "mime.type", tempString);
        } else
        {
            nds.removeArgument(tempString2, "mime.type");
        }
        tempString = (String)fileType.get("converted.filetypeid");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "converted.filetypeid") == null)
                nds.addArgument(tempString2, "converted.filetypeid", tempString);
            else
                nds.setArgument(tempString2, "converted.filetypeid", tempString);
        } else
        {
            nds.removeArgument(tempString2, "converted.filetypeid");
        }
        tempString = (String)fileType.get("converter");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "converter") == null)
                nds.addArgument(tempString2, "converter", tempString);
            else
                nds.setArgument(tempString2, "converter", tempString);
        } else
        {
            nds.removeArgument(tempString2, "converter");
        }
        fileType = null;
        return true;
    }

    public boolean removeFileType(String fileTypeId)
        throws IIPRequestException
    {
        String tempString = null;
        if(Utils.isNullString(fileTypeId))
            throw new IIPRequestException("Miss out mandatory parameter: fileTypeId");
        getClass();
        tempString = "::/DSS_SYSTEM_DIRECTORY/FILETYPE" + "/" + fileTypeId;
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            throw new IIPRequestException("File type ID not exsits: " + fileTypeId);
        } else
        {
            nds.removeNode(tempString);
            return true;
        }
    }

    public ArrayList listFileTypeId()
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/DSS_SYSTEM_DIRECTORY/FILETYPE");
    }

    public String getName(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        int index = path.lastIndexOf('/');
        if(index <= 0)
            return null;
        else
            return path.substring(index + 1);
    }

    public String getParent(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        int index = path.lastIndexOf('/');
        if(index <= 0)
            return null;
        else
            return path.substring(0, index);
    }

    public long getLastModifiedDate(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return 0L;
        File baseFile = new File(localPath);
        if(!baseFile.exists())
            return 0L;
        else
            return baseFile.lastModified();
    }

    public boolean setLastModifiedDate(String path, Long date)
        throws IIPRequestException
    {
        if(date == null || date.longValue() == 0L)
            return false;
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath);
        if(!baseFile.exists())
        {
            return false;
        } else
        {
            boolean result = false;
            result = baseFile.setLastModified(date.longValue());
            return result;
        }
    }

    public boolean isFile(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        File baseFile = new File(localPath);
        boolean result = false;
        try
        {
            result = baseFile.isFile();
        }
        catch(Exception exception) { }
        baseFile = null;
        return result;
    }

    public boolean isFolder(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
        {
            return false;
        } else
        {
            File baseFile = new File(localPath);
            return baseFile.isDirectory();
        }
    }

    public boolean isHidden(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
        {
            return false;
        } else
        {
            File baseFile = new File(localPath);
            return baseFile.isHidden();
        }
    }

    public boolean canWrite(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
        {
            return false;
        } else
        {
            File baseFile = new File(localPath);
            return baseFile.canWrite();
        }
    }

    public synchronized boolean exists(String path)
        throws IIPRequestException
    {
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return false;
        if(uploadSyncControl.get(path) != null)
            try
            {
                for(; Boolean.FALSE.equals(uploadSyncControl.get(path)); wait(500L));
                uploadSyncControl.remove(path);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        File baseFile = new File(localPath);
        return baseFile.exists();
    }

    public String getStorageId(String path)
        throws IIPRequestException
    {
        String tempPath = path.replace('/', '^');
        String storageId = null;
        getClass();
        for(storageId = nds.getValue("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/" + tempPath); storageId == null && tempPath != null;)
        {
            tempPath = getParentInternal(tempPath);
            if(tempPath.equals(""))
            {
                getClass();
                storageId = nds.getValue("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/^");
            } else
            {
                getClass();
                storageId = nds.getValue("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/" + tempPath);
            }
        }

        return storageId;
    }

    public boolean doFileConversion(String inputDir, ArrayList inputFiles, String dstFileExtension, String fileConverter)
        throws IIPRequestException
    {
        boolean status = false;
        Utils.isNullString(inputDir);
        try
        {
            inputDir = inputDir.replace('\\', '/');
            inputDir = getLocalPath(inputDir);
            Class converterClass = Class.forName("dyna.framework.service.dss." + fileConverter);
            Class parmTypes[] = {
                java.lang.String.class, java.util.ArrayList.class
            };
            Constructor cons = converterClass.getConstructor(parmTypes);
            if(cons == null)
                throw new IIPRequestException("Invalid File Converter.");
            Object parms[] = {
                inputDir, inputFiles
            };
            Object object = cons.newInstance(parms);
            if(object instanceof RemoteFileConveter)
            {
                ((RemoteFileConveter)object).setConverterName(fileConverter);
                ((RemoteFileConveter)object).setDestinationExtension(dstFileExtension);
                status = ((RemoteFileConveter)object).convert();
            } else
            {
                boolean _tmp = object instanceof FileConveter;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new IIPRequestException(e.getMessage());
        }
        return status;
    }

    public boolean remoteCheckErrFile(String inputDir, ArrayList inputFiles, String fileConverter)
        throws IIPRequestException
    {
        boolean status = false;
        Utils.isNullString(inputDir);
        try
        {
            inputDir = inputDir.replace('\\', '/');
            inputDir = getLocalPath(inputDir);
            Class converterClass = Class.forName("dyna.framework.service.dss." + fileConverter);
            Class parmTypes[] = {
                java.lang.String.class, java.util.ArrayList.class
            };
            Constructor cons = converterClass.getConstructor(parmTypes);
            if(cons == null)
                throw new IIPRequestException("Invalid File Converter.");
            Object parms[] = {
                inputDir, inputFiles
            };
            Object object = cons.newInstance(parms);
            if(object instanceof RemoteFileConveter)
            {
                ((RemoteFileConveter)object).setConverterName(fileConverter);
                status = ((RemoteFileConveter)object).errCheck();
            } else
            {
                boolean _tmp = object instanceof FileConveter;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new IIPRequestException(e.getMessage());
        }
        return status;
    }

    public int prepareInputStream(String path)
        throws IIPRequestException
    {
        int timeout;
        File baseFile;
        File baseFileTemp;
        timeout = 720;
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
            return 0;
        baseFile = new File(localPath);
        if(!baseFile.canRead())
        {
            baseFile = null;
            return 0;
        }
        getClass();
        String localPathTemp = getLocalPath(path + ".dsstemp");
        if(Utils.isNullString(localPathTemp))
        {
            baseFile = null;
            return 0;
        }
        baseFileTemp = new File(localPathTemp);
          goto _L1
_L3:
        if(--timeout >= 0)
            continue; /* Loop/switch isn't completed */
        baseFileTemp.delete();
        break; /* Loop/switch isn't completed */
        Thread.sleep(5000L);
_L1:
        if(baseFileTemp.exists()) goto _L3; else goto _L2
_L2:
        baseFileTemp.createNewFile();
        break MISSING_BLOCK_LABEL_193;
        InterruptedException e;
        e;
        baseFileTemp = null;
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        e;
        baseFile = null;
        baseFileTemp = null;
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        PrepareDownload download = new PrepareDownload(baseFile, baseFileTemp);
        int port = download.port;
        if(port == 0)
        {
            download = null;
            baseFile = null;
            baseFileTemp = null;
            return 0;
        } else
        {
            download.start();
            download = null;
            baseFile = null;
            baseFileTemp = null;
            return port;
        }
    }

    public int prepareOutputStream(String path, Long date)
        throws IIPRequestException
    {
        int timeout;
        File baseFileTemp;
        timeout = 720;
        getClass();
        String localPathTemp = getLocalPath(path + ".dsstemp");
        if(Utils.isNullString(localPathTemp))
            return 0;
        baseFileTemp = new File(localPathTemp);
          goto _L1
_L3:
        if(--timeout >= 0)
            continue; /* Loop/switch isn't completed */
        baseFileTemp.delete();
        break; /* Loop/switch isn't completed */
        Thread.sleep(5000L);
_L1:
        if(baseFileTemp.exists()) goto _L3; else goto _L2
_L2:
        baseFileTemp.createNewFile();
        break MISSING_BLOCK_LABEL_149;
        InterruptedException e;
        e;
        baseFileTemp = null;
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        e;
        baseFileTemp = null;
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        String localPath = getLocalPath(path);
        if(Utils.isNullString(localPath))
        {
            baseFileTemp = null;
            return 0;
        }
        File baseFile = new File(localPath);
        try
        {
            if(!baseFile.exists())
                baseFile.createNewFile();
            if(!baseFile.canWrite())
            {
                baseFileTemp.delete();
                baseFileTemp = null;
                baseFile = null;
                return 0;
            }
        }
        catch(IOException e)
        {
            baseFileTemp = null;
            baseFile = null;
            e.printStackTrace();
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        PrepareUpload upload = new PrepareUpload(baseFile, baseFileTemp, path);
        int port = upload.port;
        if(port == 0)
        {
            upload = null;
            baseFileTemp = null;
            baseFile = null;
            return 0;
        } else
        {
            upload.date = date;
            upload.start();
            uploadSyncControl.put(path, Boolean.FALSE);
            upload = null;
            baseFileTemp = null;
            baseFile = null;
            return port;
        }
    }

    public InputStream getInputStream(String path)
        throws IIPRequestException
    {
        return null;
    }

    public OutputStream getOutputStream(String path)
        throws IIPRequestException
    {
        return null;
    }

    public OutputStream getOutputStream(String path, Long date)
        throws IIPRequestException
    {
        return null;
    }

    public boolean downloadFile(String serverPath, String clientPath, FileTransferCallback callback)
        throws IIPRequestException
    {
        File baseFile1;
        File baseFile2;
        BufferedInputStream in;
        BufferedOutputStream out;
        String localPath1 = getLocalPath(serverPath);
        if(Utils.isNullString(localPath1))
            return false;
        String localPath2 = clientPath;
        if(Utils.isNullString(localPath2))
            return false;
        baseFile1 = new File(localPath1);
        if(!baseFile1.isFile() || !baseFile1.canRead())
        {
            baseFile1 = null;
            return false;
        }
        baseFile2 = new File(localPath2);
        if(baseFile2.isDirectory())
        {
            baseFile1 = null;
            baseFile2 = null;
            return false;
        }
        byte buffer[] = (byte[])null;
        in = null;
        out = null;
        if(!baseFile2.exists())
            baseFile2.createNewFile();
        if(!baseFile2.canWrite())
            return false;
        getClass();
        byte buffer[] = new byte[8192];
        int inBytes = 0;
        boolean finished = false;
        FileInputStream fis = new FileInputStream(baseFile1);
        getClass();
        in = new BufferedInputStream(fis, 8192);
        FileOutputStream fos = new FileOutputStream(baseFile2);
        getClass();
        out = new BufferedOutputStream(fos, 8192);
        do
        {
            getClass();
            inBytes = in.read(buffer, 0, 8192);
            if(inBytes > 0)
                out.write(buffer, 0, inBytes);
            else
                finished = true;
        } while(!finished);
        FileDescriptor fdi = fis.getFD();
        fdi.sync();
        fdi = null;
        in.close();
        in = null;
        fis.close();
        fis = null;
        out.flush();
        fos.flush();
        FileDescriptor fdo = fos.getFD();
        fdo.sync();
        fdo = null;
        out.close();
        out = null;
        fos.close();
        fos = null;
        buffer = (byte[])null;
          goto _L1
        IOException e;
        e;
        byte buffer[] = (byte[])null;
        if(in != null)
        {
            try
            {
                in.close();
            }
            catch(IOException ioexception) { }
            in = null;
        }
        if(out != null)
        {
            try
            {
                out.close();
            }
            catch(IOException ioexception1) { }
            out = null;
        }
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        local;
        baseFile1 = null;
        baseFile2 = null;
        JVM INSTR ret 17;
_L1:
        return true;
    }

    public boolean uploadFile(String serverPath, String clientPath, FileTransferCallback callback)
        throws IIPRequestException
    {
        File baseFile1;
        File baseFile2;
        BufferedInputStream in;
        BufferedOutputStream out;
        String localPath1 = clientPath;
        if(Utils.isNullString(localPath1))
            return false;
        String localPath2 = getLocalPath(serverPath);
        if(Utils.isNullString(localPath2))
            return false;
        baseFile1 = new File(localPath1);
        if(!baseFile1.isFile() || !baseFile1.canRead())
        {
            baseFile1 = null;
            return false;
        }
        baseFile2 = new File(localPath2);
        if(baseFile2.isDirectory())
        {
            baseFile1 = null;
            baseFile2 = null;
            return false;
        }
        byte buffer[] = (byte[])null;
        in = null;
        out = null;
        if(!baseFile2.exists())
            baseFile2.createNewFile();
        if(!baseFile2.canWrite())
            return false;
        getClass();
        byte buffer[] = new byte[8192];
        int inBytes = 0;
        boolean finished = false;
        FileInputStream fis = new FileInputStream(baseFile1);
        getClass();
        in = new BufferedInputStream(fis, 8192);
        FileOutputStream fos = new FileOutputStream(baseFile2);
        getClass();
        out = new BufferedOutputStream(fos, 8192);
        do
        {
            getClass();
            inBytes = in.read(buffer, 0, 8192);
            if(inBytes > 0)
                out.write(buffer, 0, inBytes);
            else
                finished = true;
        } while(!finished);
        FileDescriptor fdi = fis.getFD();
        fdi.sync();
        fdi = null;
        in.close();
        in = null;
        fis.close();
        fis = null;
        out.flush();
        fos.flush();
        FileDescriptor fdo = fos.getFD();
        fdo.sync();
        fdo = null;
        out.close();
        out = null;
        fos.close();
        fos = null;
        buffer = (byte[])null;
          goto _L1
        IOException e;
        e;
        byte buffer[] = (byte[])null;
        if(in != null)
        {
            try
            {
                in.close();
            }
            catch(IOException ioexception) { }
            in = null;
        }
        if(out != null)
        {
            try
            {
                out.close();
            }
            catch(IOException ioexception1) { }
            out = null;
        }
        e.printStackTrace();
        throw new IIPRequestException(e.fillInStackTrace().toString());
        local;
        baseFile1 = null;
        baseFile2 = null;
        JVM INSTR ret 17;
_L1:
        return true;
    }

    public boolean downloadFileForce(String serverPath, String clientPath, FileTransferCallback callback)
        throws IIPRequestException
    {
        return false;
    }

    public boolean uploadFileForce(String serverPath, String clientPath, FileTransferCallback callback)
        throws IIPRequestException
    {
        return false;
    }

    private String getParentInternal(String path)
    {
        String parent = null;
        if(path == null)
            return null;
        try
        {
            parent = path.substring(0, path.lastIndexOf('^'));
        }
        catch(Exception e)
        {
            parent = null;
        }
        return parent;
    }

    public String getLocalPath(String path)
        throws IIPRequestException
    {
        String tempPath = path.replace('/', '^');
        String storageId = null;
        getClass();
        for(storageId = nds.getValue("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/" + tempPath); storageId == null && tempPath != null;)
        {
            tempPath = getParentInternal(tempPath);
            if(tempPath.equals(""))
            {
                getClass();
                storageId = nds.getValue("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/^");
            } else
            {
                getClass();
                storageId = nds.getValue("::/DSS_SYSTEM_DIRECTORY/MOUNT" + "/" + tempPath);
            }
        }

        if(Utils.isNullString(storageId))
            return null;
        getClass();
        if(!nds.getArgument("::/DSS_SYSTEM_DIRECTORY/STORAGE" + "/" + storageId, "broker.id").equals(getBrokerId()))
            return null;
        if(tempPath.equals(""))
            tempPath = "^";
        getClass();
        return nds.getArgument("::/DSS_SYSTEM_DIRECTORY/STORAGE" + "/" + storageId, "base.directory.path") + fs + path.substring(tempPath.length()).replace('/', fs);
    }

    public String executeServerScript(String scriptFileName, String inputObject)
        throws IIPRequestException
    {
        if(Utils.isNullString(scriptFileName))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + scriptFileName);
        Object result = Utils.executeScriptFile(scriptFileName, this, inputObject, true);
        if(result != null && (result instanceof String))
            return (String)result;
        else
            return null;
    }

    public String executeServerScript(String scriptFileName)
        throws IIPRequestException
    {
        return executeServerScript(scriptFileName, null);
    }

    public int executeServerExternalProgram(String programFilePath, ArrayList arguments)
        throws IIPRequestException
    {
        if(Utils.isNullString(programFilePath))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + programFilePath);
        String commandArray[] = (String[])null;
        if(arguments == null)
        {
            commandArray = new String[1];
            commandArray[0] = programFilePath;
        } else
        {
            commandArray = new String[arguments.size() + 1];
            commandArray[0] = programFilePath;
            for(int i = 0; i < arguments.size(); i++)
                commandArray[i + 1] = (String)arguments.get(i);

        }
        int result = 0;
        Process process;
        try
        {
            process = Runtime.getRuntime().exec(commandArray);
            process.waitFor();
            result = process.exitValue();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new IIPRequestException(e.toString());
        }
        process = null;
        return result;
    }

    public int executeServerExternalProgram(String programFilePath)
        throws IIPRequestException
    {
        return executeServerExternalProgram(programFilePath, null);
    }

    private final int IO_BUFFER_SIZE = 8192;
    private final String TEMP_EXT = ".dsstemp";
    private final String NDS_BASE = "::/DSS_SYSTEM_DIRECTORY";
    private final String NDS_STORAGE = "::/DSS_SYSTEM_DIRECTORY/STORAGE";
    private final String NDS_FILETYPE = "::/DSS_SYSTEM_DIRECTORY/FILETYPE";
    private final String NDS_MOUNT = "::/DSS_SYSTEM_DIRECTORY/MOUNT";
    private NDS nds;
    private AUS aus;
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final char fs = System.getProperty("file.separator").charAt(0);
    private HashMap uploadSyncControl;

}
