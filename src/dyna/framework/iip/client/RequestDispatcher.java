// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RequestDispatcher.java

package dyna.framework.iip.client;

import dyna.framework.Client;
import dyna.framework.iip.*;
import dyna.framework.service.DSS;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.util.Utils;
import java.io.*;
import java.lang.reflect.*;
import java.net.Socket;
import java.util.HashMap;

// Referenced classes of package dyna.framework.iip.client:
//            LookupRecord, Requester

public class RequestDispatcher
    implements InvocationHandler
{
    private class LogicalInputStream extends InputStream
    {

        public int read()
            throws IOException
        {
            return in.read();
        }

        public int read(byte b[])
            throws IOException
        {
            return in.read(b);
        }

        public int read(byte b[], int off, int len)
            throws IOException
        {
            return in.read(b, off, len);
        }

        public long skip(long n)
            throws IOException
        {
            return in.skip(n);
        }

        public int available()
            throws IOException
        {
            return in.available();
        }

        public void mark(int readlimit)
        {
            in.mark(readlimit);
        }

        public void reset()
            throws IOException
        {
            in.reset();
        }

        public boolean markSupported()
        {
            return in.markSupported();
        }

        public void close()
            throws IOException
        {
            in.close();
            in = null;
            socket.close();
            socket = null;
        }

        private InputStream in;
        private Socket socket;

        public LogicalInputStream(Socket socket)
            throws IOException
        {
            in = null;
            this.socket = null;
            this.socket = socket;
            in = this.socket.getInputStream();
        }
    }

    private class LogicalOutputStream extends OutputStream
    {

        public void write(int b)
            throws IOException
        {
            out.write(b);
        }

        public void write(byte b[])
            throws IOException
        {
            out.write(b);
        }

        public void write(byte b[], int off, int len)
            throws IOException
        {
            out.write(b, off, len);
        }

        public void flush()
            throws IOException
        {
            out.flush();
        }

        public void close()
            throws IOException
        {
            out.close();
            out = null;
            socket.close();
            socket = null;
        }

        private OutputStream out;
        private Socket socket;

        public LogicalOutputStream(Socket socket)
            throws IOException
        {
            out = null;
            this.socket = null;
            this.socket = socket;
            out = this.socket.getOutputStream();
        }
    }


    public RequestDispatcher()
    {
        client = null;
        requester = null;
        accessIdentifier = 0L;
        interfaceCache = null;
        client = Client.client;
    }

    public synchronized void setAccessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        interfaceCache = new HashMap();
        requester = client.getRequester(accessIdentifier);
        System.out.println(" [initiated]");
    }

    public Service getServiceInstance(String serviceId)
        throws ServiceNotFoundException
    {
        if(Utils.isNullString(serviceId))
            throw new ServiceNotFoundException(serviceId);
        LookupRecord lookupRecord = lookup(serviceId);
        if(lookupRecord == null)
            throw new ServiceNotFoundException(serviceId);
        interfaceCache.put(lookupRecord.serviceInterface, lookupRecord);
        Service service = null;
        if(lookupRecord.service == null)
        {
            Class proxyInterfaces[] = {
                lookupRecord.serviceInterface
            };
            service = (Service)Proxy.newProxyInstance(lookupRecord.serviceInterface.getClassLoader(), proxyInterfaces, this);
            proxyInterfaces = (Class[])null;
            lookupRecord.service = service;
        } else
        {
            service = lookupRecord.service;
        }
        lookupRecord = null;
        return service;
    }

    public Object invoke(Object proxy, Method method, Object args[])
        throws Throwable
    {
        Class interfaceClass = method.getDeclaringClass();
        LookupRecord lookupRecord = (LookupRecord)interfaceCache.get(interfaceClass);
        String serviceName = interfaceClass.getName();
        if(lookupRecord == null)
            throw new ServiceNotFoundException(serviceName);
        interfaceClass = null;
        Object result = null;
        try
        {
            if(serviceName.equals("dyna.framework.service.DSS"))
            {
                String methodName = method.getName();
                DSS dss = (DSS)lookupRecord.service;
                if(methodName.equals("getInputStream"))
                {
                    int port = dss.prepareInputStream((String)args[0]);
                    if(port < 1)
                    {
                        result = null;
                    } else
                    {
                        Socket socket = new Socket(lookupRecord.ipAddress, port);
                        result = new LogicalInputStream(socket);
                        socket = null;
                    }
                } else
                if(methodName.equals("getOutputStream"))
                {
                    int port = 0;
                    if(args.length == 2)
                        port = dss.prepareOutputStream((String)args[0], (Long)args[1]);
                    else
                    if(args.length == 1)
                        port = dss.prepareOutputStream((String)args[0], new Long(System.currentTimeMillis()));
                    if(port < 1)
                    {
                        result = null;
                    } else
                    {
                        Socket socket = new Socket(lookupRecord.ipAddress, port);
                        result = new LogicalOutputStream(socket);
                        socket = null;
                    }
                } else
                {
                    if(methodName.equals("downloadFile"))
                    {
                        InputStream in = null;
                        OutputStream out = null;
                        String filePath = (String)args[1];
                        File file = null;
                        File fileTemp = null;
                        boolean finished = false;
                        int inBytes = 0;
                        byte buffer[] = (byte[])null;
                        FileTransferCallback ftc = null;
                        if(Utils.isNullString((String)args[0]) || Utils.isNullString(filePath))
                            return Utils.False;
                        try
                        {
                            getClass();
                            fileTemp = new File(filePath + ".dsstemp");
                            if(fileTemp.exists())
                                return Utils.False;
                            in = dss.getInputStream((String)args[0]);
                            if(in == null)
                                return Utils.False;
                            fileTemp.createNewFile();
                            if(!fileTemp.canWrite())
                            {
                                fileTemp = null;
                                return Utils.False;
                            }
                            file = new File(filePath);
                            if(file.exists())
                            {
                                fileTemp.delete();
                                fileTemp = null;
                                file = null;
                                return Utils.False;
                            }
                            file.createNewFile();
                            if(!file.canWrite())
                            {
                                fileTemp.delete();
                                fileTemp = null;
                                file = null;
                                return Utils.False;
                            }
                            file.delete();
                            buffer = new byte[8192];
                            out = new BufferedOutputStream(new FileOutputStream(fileTemp), 8192);
                            ftc = (FileTransferCallback)args[2];
                            do
                            {
                                inBytes = in.read(buffer, 0, 8192);
                                if(inBytes > 0)
                                {
                                    out.write(buffer, 0, inBytes);
                                    if(ftc != null)
                                        ftc.transferBytes(inBytes);
                                } else
                                {
                                    finished = true;
                                }
                            } while(!finished);
                            buffer = (byte[])null;
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;
                            fileTemp.setLastModified(dss.getLastModifiedDate((String)args[0]));
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
                            throw new IIPRequestException(e.fillInStackTrace().toString());
                        }
                        return Utils.True;
                    }
                    if(methodName.equals("uploadFile"))
                    {
                        InputStream in = null;
                        OutputStream out = null;
                        String filePath = (String)args[1];
                        File file = null;
                        File fileTemp = null;
                        boolean finished = false;
                        int inBytes = 0;
                        byte buffer[] = (byte[])null;
                        FileTransferCallback ftc = null;
                        if(Utils.isNullString((String)args[0]) || Utils.isNullString(filePath))
                            return Utils.False;
                        try
                        {
                            getClass();
                            fileTemp = new File(filePath + ".dsstemp");
                            if(fileTemp.exists())
                                return Utils.False;
                            fileTemp.createNewFile();
                            if(!fileTemp.canWrite())
                            {
                                fileTemp = null;
                                return Utils.False;
                            }
                            file = new File(filePath);
                            if(!file.exists())
                            {
                                fileTemp.delete();
                                fileTemp = null;
                                file = null;
                                return Utils.False;
                            }
                            buffer = new byte[8192];
                            in = new BufferedInputStream(new FileInputStream(file), 8192);
                            out = dss.getOutputStream((String)args[0], new Long(file.lastModified()));
                            ftc = (FileTransferCallback)args[2];
                            do
                            {
                                inBytes = in.read(buffer, 0, 8192);
                                if(inBytes > 0)
                                {
                                    out.write(buffer, 0, inBytes);
                                    if(ftc != null)
                                        ftc.transferBytes(inBytes);
                                } else
                                {
                                    finished = true;
                                }
                            } while(!finished);
                            buffer = (byte[])null;
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;
                            fileTemp.delete();
                            fileTemp = null;
                            file = null;
                            dss.exists((String)args[0]);
                        }
                        catch(IOException e)
                        {
                            if(in != null)
                            {
                                try
                                {
                                    in.close();
                                }
                                catch(IOException ioexception2) { }
                                in = null;
                            }
                            if(out != null)
                            {
                                try
                                {
                                    out.close();
                                }
                                catch(IOException ioexception3) { }
                                out = null;
                            }
                            buffer = (byte[])null;
                            fileTemp = null;
                            file = null;
                            throw new IIPRequestException(e.fillInStackTrace().toString());
                        }
                        return Utils.True;
                    }
                    if(methodName.equals("downloadFileForce"))
                    {
                        InputStream in = null;
                        OutputStream out = null;
                        String filePath = (String)args[1];
                        File file = null;
                        File fileTemp = null;
                        boolean finished = false;
                        int inBytes = 0;
                        byte buffer[] = (byte[])null;
                        FileTransferCallback ftc = null;
                        if(Utils.isNullString((String)args[0]) || Utils.isNullString(filePath))
                            return Utils.False;
                        try
                        {
                            getClass();
                            fileTemp = new File(filePath + ".dsstemp");
                            if(fileTemp.exists())
                                fileTemp.delete();
                            in = dss.getInputStream((String)args[0]);
                            if(in == null)
                                return Utils.False;
                            fileTemp.createNewFile();
                            if(!fileTemp.canWrite())
                            {
                                fileTemp = null;
                                return Utils.False;
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
                                return Utils.False;
                            }
                            file.delete();
                            buffer = new byte[8192];
                            out = new BufferedOutputStream(new FileOutputStream(fileTemp), 8192);
                            ftc = (FileTransferCallback)args[2];
                            do
                            {
                                inBytes = in.read(buffer, 0, 8192);
                                if(inBytes > 0)
                                {
                                    out.write(buffer, 0, inBytes);
                                    if(ftc != null)
                                        ftc.transferBytes(inBytes);
                                } else
                                {
                                    finished = true;
                                }
                            } while(!finished);
                            buffer = (byte[])null;
                            in.close();
                            in = null;
                            out.close();
                            out = null;
                            fileTemp.setLastModified(dss.getLastModifiedDate((String)args[0]));
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
                                catch(IOException ioexception4) { }
                                in = null;
                            }
                            if(out != null)
                            {
                                try
                                {
                                    out.close();
                                }
                                catch(IOException ioexception5) { }
                                out = null;
                            }
                            buffer = (byte[])null;
                            fileTemp = null;
                            file = null;
                            throw new IIPRequestException(e.fillInStackTrace().toString());
                        }
                        return Utils.True;
                    }
                    if(methodName.equals("uploadFileForce"))
                    {
                        InputStream in = null;
                        OutputStream out = null;
                        String filePath = (String)args[1];
                        File file = null;
                        File fileTemp = null;
                        boolean finished = false;
                        int inBytes = 0;
                        byte buffer[] = (byte[])null;
                        FileTransferCallback ftc = null;
                        if(Utils.isNullString((String)args[0]) || Utils.isNullString(filePath))
                            return Utils.False;
                        try
                        {
                            getClass();
                            fileTemp = new File(filePath + ".dsstemp");
                            if(fileTemp.exists())
                                fileTemp.delete();
                            fileTemp.createNewFile();
                            if(!fileTemp.canWrite())
                            {
                                fileTemp = null;
                                return Utils.False;
                            }
                            file = new File(filePath);
                            if(!file.exists())
                            {
                                fileTemp.delete();
                                fileTemp = null;
                                file = null;
                                return Utils.False;
                            }
                            buffer = new byte[8192];
                            in = new BufferedInputStream(new FileInputStream(file), 8192);
                            out = dss.getOutputStream((String)args[0], new Long(file.lastModified()));
                            ftc = (FileTransferCallback)args[2];
                            do
                            {
                                inBytes = in.read(buffer, 0, 8192);
                                if(inBytes > 0)
                                {
                                    out.write(buffer, 0, inBytes);
                                    if(ftc != null)
                                        ftc.transferBytes(inBytes);
                                } else
                                {
                                    finished = true;
                                }
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
                                catch(IOException ioexception6) { }
                                in = null;
                            }
                            if(out != null)
                            {
                                try
                                {
                                    out.close();
                                }
                                catch(IOException ioexception7) { }
                                out = null;
                            }
                            buffer = (byte[])null;
                            fileTemp = null;
                            file = null;
                            throw new IIPRequestException(e.fillInStackTrace().toString());
                        }
                        return Utils.True;
                    }
                    result = requester.request(lookupRecord.serviceId, method.getName(), ((Object) (args)));
                }
                dss = null;
            } else
            {
                result = requester.request(lookupRecord.serviceId, method.getName(), ((Object) (args)));
            }
        }
        catch(Exception e)
        {
            lookupRecord = null;
            if(e instanceof IIPRequestException)
                throw e;
            else
                throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        lookupRecord = null;
        return result;
    }

    private LookupRecord lookup(String serviceId)
    {
        LookupRecord lookupRecord = client.lookupService(accessIdentifier, serviceId);
        if(lookupRecord == null)
            return null;
        while(lookupRecord.socket == null) 
            try
            {
                lookupRecord.socket = new Socket(lookupRecord.ipAddress, lookupRecord.port);
                lookupRecord.out = new DataOutputStream(new BufferedOutputStream(lookupRecord.socket.getOutputStream(), 8192));
                lookupRecord.in = new DataInputStream(new BufferedInputStream(lookupRecord.socket.getInputStream(), 8192));
            }
            catch(IOException e)
            {
                client.removeLookupRecord(accessIdentifier, serviceId);
                lookupRecord = client.lookupService(accessIdentifier, serviceId);
            }
        return lookupRecord;
    }

    private final String TEMP_EXT = ".dsstemp";
    private Client client;
    private Requester requester;
    private long accessIdentifier;
    private HashMap interfaceCache;
}
