// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RemoteFileConveter.java

package dyna.framework.service.dss;

import dyna.framework.iip.IIPRequestException;
import dyna.util.Utils;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public abstract class RemoteFileConveter
{

    public RemoteFileConveter(String inputDir, ArrayList inputFiles)
    {
        this.inputDir = null;
        converterName = null;
        serverAddress = null;
        serverPort = null;
        soTimeout = null;
        this.inputFiles = null;
        srcExtension = null;
        dstExtension = null;
        this.inputDir = inputDir;
        this.inputFiles = inputFiles;
    }

    public boolean convert()
        throws IIPRequestException
    {
        if(Utils.isNullString(inputDir) || Utils.isNullArrayList(inputFiles) || Utils.isNullString(converterName))
            throw new IIPRequestException("RemoteFileConverter: Missing input parameter(s).");
        String converterPath = getConverterPath();
        if(Utils.isNullString(converterPath))
            throw new IIPRequestException("RemoteFileConverter: Converter not defined.");
        try
        {
            Properties property = new Properties();
            property.load(new FileInputStream(converterName + ".ini"));
            serverAddress = property.getProperty("serverAddress");
            serverPort = property.getProperty("serverPort");
            soTimeout = property.getProperty("soTimeout");
            property.clear();
            property = null;
        }
        catch(Exception e)
        {
            throw new IIPRequestException(e.toString());
        }
        if(Utils.isNullString(serverAddress) || Utils.isNullString(serverPort))
            throw new IIPRequestException("RemoteFileConverter: Can not find server addresss.");
        boolean status = true;
        String inputFileName = null;
        for(Iterator fileKey = inputFiles.iterator(); fileKey.hasNext();)
        {
            inputFileName = (String)fileKey.next();
            System.out.println("Upload Source File.");
            status = uploadFile(inputFileName);
            if(!status)
            {
                System.err.println("[ERROR] File Upload Failure.");
            } else
            {
                status = remoteConvert(inputFileName);
                if(!status)
                {
                    System.err.println("[ERROR] File Conversion Failure.");
                } else
                {
                    System.out.println("Download Converted File.");
                    status = downloadFile(inputFileName);
                    if(!status)
                        System.err.println("[ERROR] File Download Failure.");
                }
            }
        }

        return status;
    }

    private boolean uploadFile(String filePath)
    {
        InputStream in = null;
        OutputStream out = null;
        File file = null;
        File fileTemp = null;
        boolean finished = false;
        int inBytes = 0;
        byte buffer[] = (byte[])null;
        try
        {
            filePath = inputDir + fs + filePath;
            getClass();
            fileTemp = new File(filePath + ".dsstemp");
            if(fileTemp.exists())
                return false;
            fileTemp.createNewFile();
            if(!fileTemp.canWrite())
            {
                fileTemp = null;
                return false;
            }
            file = new File(filePath);
            if(!file.exists())
            {
                fileTemp.delete();
                fileTemp = null;
                file = null;
                return false;
            }
            buffer = new byte[8192];
            Socket socket = null;
            String inputString = null;
            Integer tempInteger = null;
            socket = new Socket(serverAddress, (new Integer(serverPort)).intValue());
            if(socket == null)
                return false;
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            writer.println("CMD_PREPAREUPLOAD");
            writer.println(file.getName());
            writer.println(file.lastModified());
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inputString = reader.readLine();
            writer.close();
            writer = null;
            reader.close();
            reader = null;
            if(Utils.isNullString(inputString))
                return false;
            tempInteger = new Integer(inputString);
            if(tempInteger == null)
                return false;
            int port = tempInteger.intValue();
            if(port < 1)
                return false;
            socket.close();
            socket = null;
            socket = new Socket(serverAddress, port);
            out = socket.getOutputStream();
            socket = null;
            in = new BufferedInputStream(new FileInputStream(file), 8192);
            do
            {
                inBytes = in.read(buffer, 0, 8192);
                if(inBytes > 0)
                    out.write(buffer, 0, inBytes);
                else
                    finished = true;
            } while(!finished);
            buffer = (byte[])null;
            in.close();
            in = null;
            out.close();
            out = null;
            fileTemp.delete();
            fileTemp = null;
            file = null;
        }
        catch(IOException e)
        {
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
            buffer = (byte[])null;
            fileTemp = null;
            file = null;
            return false;
        }
        return true;
    }

    private boolean downloadFile(String filePath)
    {
        InputStream in = null;
        OutputStream out = null;
        File file = null;
        File fileTemp = null;
        boolean finished = false;
        int inBytes = 0;
        byte buffer[] = (byte[])null;
        if(filePath == null || "".equals(filePath))
            return false;
        try
        {
            filePath = inputDir + fs + filePath;
            filePath = filePath + "." + dstExtension;
            getClass();
            fileTemp = new File(filePath + ".dsstemp");
            if(fileTemp.exists())
                fileTemp.delete();
            fileTemp.createNewFile();
            if(!fileTemp.canWrite())
            {
                fileTemp = null;
                return false;
            }
            file = new File(filePath);
            if(file.exists())
                file.delete();
            file.createNewFile();
            if(!file.canWrite())
            {
                fileTemp.delete();
                fileTemp = null;
                file = null;
                return false;
            }
            file.delete();
            Socket socket = null;
            String inputString = null;
            Integer tempInteger = null;
            socket = new Socket(serverAddress, (new Integer(serverPort)).intValue());
            if(socket == null)
                return false;
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            writer.println("CMD_PREPAREDOWNLOAD");
            writer.println(file.getName());
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inputString = reader.readLine();
            writer.close();
            writer = null;
            reader.close();
            reader = null;
            if(Utils.isNullString(inputString))
                return false;
            tempInteger = new Integer(inputString);
            if(tempInteger == null)
                return false;
            int port = tempInteger.intValue();
            if(port < 1)
                return false;
            socket.close();
            socket = null;
            socket = new Socket(serverAddress, port);
            if(socket == null)
                return false;
            buffer = new byte[8192];
            in = new BufferedInputStream(socket.getInputStream(), 8192);
            out = new BufferedOutputStream(new FileOutputStream(fileTemp), 8192);
            do
            {
                inBytes = in.read(buffer, 0, 8192);
                if(inBytes > 0)
                    out.write(buffer, 0, inBytes);
                else
                    finished = true;
            } while(!finished);
            buffer = (byte[])null;
            in.close();
            in = null;
            out.close();
            out = null;
            if(fileTemp.length() == 0L)
                fileTemp.delete();
            else
                fileTemp.renameTo(file);
            fileTemp = null;
            file = null;
        }
        catch(IOException e)
        {
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
            buffer = (byte[])null;
            fileTemp = null;
            file = null;
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setConverterName(String converterName)
    {
        this.converterName = converterName;
    }

    public String getConverterName()
    {
        return converterName;
    }

    public void setSourceExtension(String extension)
    {
        srcExtension = extension;
    }

    public void setDestinationExtension(String extension)
    {
        dstExtension = extension;
    }

    private boolean remoteConvert(String filePath)
    {
        Socket socket;
        socket = null;
        String inputString = null;
        Integer tempInteger = null;
        socket = new Socket(serverAddress, (new Integer(serverPort)).intValue());
        if(socket == null)
            return false;
        boolean flag;
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        writer.println("CMD_CONVERT");
        writer.println(getConverterPath() + dstExtension);
        writer.println(filePath);
        writer.flush();
        if(soTimeout != null || !"".equals(soTimeout))
        {
            int timeout = (new Integer(soTimeout)).intValue();
            socket.setSoTimeout(timeout * 60 * 1000);
            System.out.println("(Timeout: " + soTimeout + " Min.)");
        }
        DataInputStream reader = new DataInputStream(socket.getInputStream());
        boolean returnValue = reader.readBoolean();
        reader.close();
        reader = null;
        writer.close();
        writer = null;
        socket.close();
        socket = null;
        flag = returnValue;
        return flag;
        SocketTimeoutException ste;
        ste;
        System.err.println("[ERROR] Remote Conversion Timeout.");
          goto _L1
        Exception e;
        e;
        e.printStackTrace();
          goto _L1
        local;
        if(socket != null && !socket.isClosed())
            try
            {
                socket.close();
            }
            catch(Exception exception1) { }
        socket = null;
        JVM INSTR ret 8;
_L1:
        return false;
    }

    public boolean errCheck()
        throws IIPRequestException
    {
        if(Utils.isNullString(inputDir) || Utils.isNullArrayList(inputFiles) || Utils.isNullString(converterName))
            throw new IIPRequestException("RemoteFileConverter: Missing input parameter(s).");
        String converterPath = getConverterPath();
        if(Utils.isNullString(converterPath))
            throw new IIPRequestException("RemoteFileConverter: Converter not defined.");
        try
        {
            Properties property = new Properties();
            property.load(new FileInputStream(converterName + ".ini"));
            serverAddress = property.getProperty("serverAddress");
            serverPort = property.getProperty("serverPort");
            soTimeout = property.getProperty("soTimeout");
            property.clear();
            property = null;
        }
        catch(Exception e)
        {
            throw new IIPRequestException(e.toString());
        }
        if(Utils.isNullString(serverAddress) || Utils.isNullString(serverPort))
            throw new IIPRequestException("RemoteFileConverter: Can not find server addresss.");
        boolean status = true;
        String inputFileName = null;
        for(Iterator fileKey = inputFiles.iterator(); fileKey.hasNext();)
        {
            inputFileName = (String)fileKey.next();
            status = isErrFile(inputFileName);
        }

        return status;
    }

    private boolean isErrFile(String filePath)
    {
        InputStream in = null;
        OutputStream out = null;
        File file = null;
        boolean finished = false;
        boolean errCheck = false;
        int inBytes = 0;
        byte buffer[] = (byte[])null;
        if(filePath == null || "".equals(filePath))
            return false;
        try
        {
            getClass();
            filePath = filePath + "." + "err";
            Socket socket = null;
            String inputString = null;
            Integer tempInteger = null;
            socket = new Socket(serverAddress, (new Integer(serverPort)).intValue());
            if(socket == null)
            {
                return false;
            } else
            {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                writer.println("CMD_ERRCHECK");
                writer.println(filePath);
                writer.flush();
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                boolean returnValue = reader.readBoolean();
                writer.close();
                writer = null;
                reader.close();
                reader = null;
                return returnValue;
            }
        }
        catch(IOException e)
        {
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
            buffer = (byte[])null;
            file = null;
            e.printStackTrace();
            return false;
        }
    }

    protected abstract String getConverterPath();

    private final char fs = System.getProperty("file.separator").charAt(0);
    private final String TEMP_EXT = ".dsstemp";
    private final String CMD_PREPAREDOWNLOAD = "CMD_PREPAREDOWNLOAD";
    private final String CMD_PREPAREUPLOAD = "CMD_PREPAREUPLOAD";
    private final String CMD_CONVERT = "CMD_CONVERT";
    private final String CMD_ERRCHECK = "CMD_ERRCHECK";
    private final String errExtension = "err";
    private String inputDir;
    private String converterName;
    private String serverAddress;
    private String serverPort;
    private String soTimeout;
    private ArrayList inputFiles;
    private String srcExtension;
    private String dstExtension;
}
