// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PCSImpl.java

package dyna.framework.service;

import dyna.framework.Server;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.bjs.Schedule;
import dyna.framework.service.dos.DOSChangeable;
import dyna.renesas.BundleDrawingIssue;
import dyna.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// Referenced classes of package dyna.framework.service:
//            PCS, DOS, DTM, DSS, 
//            NDS

public class PCSImpl extends ServiceServer
    implements PCS, Runnable
{

    public PCSImpl()
    {
        dos = null;
        dtm = null;
        nds = null;
        dss = null;
        dlock = new DaemonLock();
        jobs = new ArrayList(100);
        plotList = new ArrayList(100);
        printList = null;
        seperatePLInfo = null;
        realMap = new HashMap();
        responseFileInfo = null;
        bdi = null;
        backupPath = null;
        plotPath = null;
        tmpPath = null;
        main_flg = false;
        printComplete = true;
        tmpOutputRequestNO = "gd0080";
        cpls = false;
        pls = 0;
        cdr = false;
        server = Server.server;
        portString = null;
        serverSocket = null;
        try
        {
            portString = "7959";
            Integer tempInteger = new Integer(portString);
            if(tempInteger == null)
            {
                System.err.println("[ERROR] Invalid Port Number.");
                System.exit(-1);
            }
            port = tempInteger.intValue();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dos = (DOS)getServiceInstance("DF30DOS1");
            dtm = (DTM)getServiceInstance("DF30DTM1");
            dss = (DSS)getServiceInstance("DF30DSS1");
            nds = (NDS)getServiceInstance("DF30NDS1");
            bdi = new BundleDrawingIssue(dos, dss, nds);
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
        }
        clearTempFolder();
        tp = server.getThreadPool(accessIdentifier);
        Thread js = new Thread(this);
        js.setDaemon(false);
        js.start();
    }

    public void run()
    {
        try
        {
            for(serverSocket = new ServerSocket(port); serverSocket != null;)
            {
                Socket socket = serverSocket.accept();
                interpretCommand(socket);
            }

            System.out.println("[WARN] Server Socket Closed.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void interpretCommand(Socket socket)
    {
        if(socket == null)
            return;
        String commandString = null;
        try
        {
            plotPath = nds.getValue("::/RENESAS_SIRIUS_PLOT_FOLDER");
            backupPath = nds.getValue("::/RENESAS_SIRIUS_BACKUP_FOLDER");
            tmpPath = nds.getValue("::/RENESAS_SIRIUS_TEMP_FOLDER");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if(reader == null)
                return;
            commandString = reader.readLine();
            if(commandString == null || "".equals(commandString))
            {
                reader.close();
                reader = null;
                System.err.println("Can not receive request...");
                return;
            }
            if(commandString.startsWith("DEL"))
                deleteOrder(commandString);
            else
            if(commandString.equals("request"))
                fileCheck(socket, reader);
            else
            if(commandString.equals("print"))
            {
                if(!reader.ready())
                {
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    commandString = reader1.readLine();
                } else
                {
                    commandString = reader.readLine();
                    printOrder(socket, reader);
                }
            } else
            if(commandString.equals("pstop"))
                stopPrint(socket, reader);
            else
            if(commandString.equals("frequest"))
                fFileCheck(socket, reader);
            reader.close();
            reader = null;
            socket.close();
            socket = null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(socket != null)
                try
                {
                    socket.close();
                }
                catch(Exception ie)
                {
                    ie.printStackTrace();
                }
            socket = null;
        }
    }

    public void fileCheck(Socket socket, BufferedReader reader)
        throws IIPRequestException
    {
        ArrayList plotList = new ArrayList();
        plotList = getPlotFileList();
        String isExist = null;
        try
        {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            responseFileInfo = new ArrayList();
            if(plotList != null && plotList.size() > 0)
            {
                isExist = "FO";
                addPrintList();
                writer.println(isExist);
                writer.println(responseFileInfo.size());
                for(int i = 0; responseFileInfo.size() > i; i++)
                    writer.println(responseFileInfo.get(i));

                responseFileInfo.clear();
                responseFileInfo = null;
            } else
            {
                isExist = "NN";
                writer.println(isExist);
            }
            writer.flush();
            String commandString = reader.readLine();
            if(commandString.equals("print"))
                printOrder(socket, reader);
            writer.flush();
            writer.close();
            writer = null;
            return;
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void printOrder(Socket socket, BufferedReader reader)
        throws IIPRequestException
    {
        try
        {
            ArrayList tmpArray = new ArrayList();
            HashMap tmpMap = null;
            ArrayList BBList = null;
            String outputMedium = null;
            String orderFileName = null;
            String DirectionNO = null;
            String BBInfoString = null;
            Hashtable sheet_size = null;
            Hashtable noChange = null;
            String drawingNO = null;
            String drawingCategory = null;
            String purchaseCategory = null;
            String productName = null;
            String outputFormula = null;
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            if(printComplete)
            {
                if(cdr)
                    writer.println("CR");
                else
                    writer.println("PO");
                writer.flush();
                String commandString = reader.readLine();
                if(commandString != null && commandString.startsWith("DEL"))
                {
                    deleteOrder(commandString);
                    cdr = false;
                    commandString = null;
                }
                if(!reader.ready())
                {
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String commandString2 = reader1.readLine();
                    if(commandString2 != null)
                        deleteOrder(commandString2);
                } else
                {
                    String commandString2 = reader.readLine();
                    deleteOrder(commandString2);
                }
                if(commandString != null && !commandString.equals(""))
                {
                    List list1 = Utils.tokenizeMessage(commandString, ':');
                    String OutputRequestNO = (String)list1.get(0);
                    int arrDrawingNO = Integer.parseInt((String)list1.get(1));
                    tmpMap = (HashMap)realMap.get(OutputRequestNO);
                    int i = 0;
                    if(tmpMap == null)
                        return;
                    i = Integer.parseInt((String)tmpMap.get("NOofOutput"));
                    if(arrDrawingNO > 0)
                    {
                        BBList = (ArrayList)tmpMap.get("BBInfo");
                        if(BBList.size() - arrDrawingNO < 0)
                            return;
                        BBInfoString = (String)BBList.get(BBList.size() - arrDrawingNO);
                        if(BBInfoString != null && !BBInfoString.equals(""))
                        {
                            List list = Utils.tokenizeMessageWithNoTrim(BBInfoString, ',');
                            drawingNO = (String)list.get(1);
                            drawingCategory = (String)list.get(2);
                            purchaseCategory = (String)list.get(3);
                            productName = (String)list.get(4);
                        }
                        outputMedium = (String)tmpMap.get("OutputMedium");
                        orderFileName = tmpPath + (String)tmpMap.get("orderName");
                        DirectionNO = (String)tmpMap.get("DirectionNO");
                        sheet_size = new Hashtable();
                        sheet_size.put("A1", tmpMap.get("A1"));
                        sheet_size.put("A2", tmpMap.get("A2"));
                        sheet_size.put("A3", tmpMap.get("A3"));
                        sheet_size.put("A4", tmpMap.get("A4"));
                        noChange = new Hashtable();
                        noChange.put("A1", "A1");
                        noChange.put("A2", "A2");
                        noChange.put("A3", "A3");
                        noChange.put("A4", "A4");
                        outputFormula = (String)tmpMap.get("OutputFormula");
                        if(outputFormula.equals("01"))
                            sheet_size = noChange;
                        if(outputMedium.equals("01"))
                        {
                            printComplete = false;
                            if(tmpOutputRequestNO.equals(OutputRequestNO))
                            {
                                if(cpls)
                                {
                                    main_flg = false;
                                    main_flg = bdi.out(OutputRequestNO, orderFileName, main_flg);
                                    tmpOutputRequestNO = OutputRequestNO;
                                    if(main_flg)
                                        main_flg = bdi.out(OutputRequestNO, orderFileName, main_flg);
                                    cpls = false;
                                    pls++;
                                }
                            } else
                            {
                                main_flg = false;
                                main_flg = bdi.out(OutputRequestNO, orderFileName, main_flg);
                                tmpOutputRequestNO = OutputRequestNO;
                                if(main_flg)
                                    main_flg = bdi.out(OutputRequestNO, orderFileName, main_flg);
                                pls = 1;
                            }
                            if(main_flg)
                                printComplete = bdi.out(drawingNO, DirectionNO, sheet_size);
                        } else
                        {
                            cdr = true;
                            printComplete = bdi.out(OutputRequestNO, orderFileName);
                        }
                    } else
                    if(i >= pls)
                        cpls = true;
                }
            } else
            {
                writer.println("PN");
            }
            writer.flush();
            writer.close();
            writer = null;
            sheet_size = null;
            return;
        }
        catch(Exception ie)
        {
            ie.printStackTrace();
        }
    }

    public void stopPrint(Socket socket, BufferedReader reader)
        throws IIPRequestException
    {
        try
        {
            reader.close();
            reader = null;
            socket.close();
            socket = null;
            return;
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void fFileCheck(Socket socket, BufferedReader reader)
        throws IIPRequestException
    {
        try
        {
            ArrayList backList = new ArrayList();
            backList = getBackFileList();
            String isExist = null;
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            responseFileInfo = new ArrayList();
            tmpOutputRequestNO = "gd0080";
            clearTempFolder();
            if(backList != null && backList.size() > 0)
            {
                isExist = "FO";
                addBackPrintList();
                writer.println(isExist);
                writer.println(responseFileInfo.size());
                for(int i = 0; responseFileInfo.size() > i; i++)
                    writer.println(responseFileInfo.get(i));

                responseFileInfo.clear();
                responseFileInfo = null;
            } else
            {
                fileCheck(socket, reader);
            }
            writer.flush();
            writer.close();
            writer = null;
            return;
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public synchronized long runJobs()
        throws IIPRequestException
    {
        long minDiff = 0x7fffffffffffffffL;
        long now = System.currentTimeMillis();
        for(int i = 0; i < jobs.size();)
        {
            Schedule jn = (Schedule)jobs.get(i);
            if(jn.executeAt.getTime() <= now)
            {
                if(jn.job == null)
                {
                    Class targetClass = null;
                    Object targetObject = null;
                    try
                    {
                        targetClass = Class.forName(jn.classPath);
                        targetObject = targetClass.newInstance();
                        jn.targetObject = targetObject;
                        jn.job = jn;
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                        jn.job = null;
                    }
                }
                if(tp != null && jn.job != null)
                    tp.addRequest(jn.job);
                else
                if(jn.job != null)
                {
                    Thread jt = new Thread(jn.job);
                    jt.setDaemon(false);
                    jt.start();
                }
            } else
            {
                long diff = jn.executeAt.getTime() - now;
                minDiff = Math.min(diff, minDiff);
                i++;
            }
        }

        return minDiff;
    }

    public ArrayList getBackFileList()
        throws IIPRequestException
    {
        File baseFile = new File(tmpPath);
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

    public ArrayList getPlotFileList()
        throws IIPRequestException
    {
        File baseFile = new File(plotPath);
        if(!baseFile.isDirectory())
            return null;
        ArrayList arrayList = null;
        File files[] = baseFile.listFiles();
        if(files != null && files.length > 0)
        {
            arrayList = new ArrayList(files.length);
            for(int i = 0; i < files.length; i++)
                if(files[i].isFile())
                {
                    String tmpString = files[i].getName();
                    List l = Utils.tokenizeMessage(tmpString, '.');
                    String tmpExtension = (String)l.get(1);
                    if(tmpExtension.toLowerCase().equals("dat"))
                        arrayList.add(tmpString);
                }

            if(arrayList.size() == 0)
                arrayList = null;
        }
        files = (File[])null;
        baseFile = null;
        return arrayList;
    }

    public void addPrintList()
        throws IIPRequestException
    {
        ArrayList addList = getPlotFileList();
        String tmpString = null;
        Iterator listKey = addList.iterator();
        if(printList == null || printList.size() == 0)
        {
            printList = addList;
            for(; listKey.hasNext(); backupFile(tmpString))
                tmpString = (String)listKey.next();

        } else
        {
            for(; listKey.hasNext(); backupFile(tmpString))
            {
                tmpString = (String)listKey.next();
                printList.add(tmpString);
            }

        }
        listKey = null;
    }

    public void addBackPrintList()
        throws IIPRequestException
    {
        ArrayList addList = getBackFileList();
        String tmpString = null;
        if(Utils.isNullArrayList(addList))
            return;
        Iterator listKey = addList.iterator();
        if(printList == null || printList.size() == 0)
        {
            printList = addList;
            String newPath;
            for(; listKey.hasNext(); readPLFileInfo(newPath, tmpString))
            {
                tmpString = (String)listKey.next();
                newPath = tmpPath + tmpString;
            }

        } else
        {
            String newPath;
            for(; listKey.hasNext(); readPLFileInfo(newPath, tmpString))
            {
                tmpString = (String)listKey.next();
                printList.add(tmpString);
                newPath = tmpPath + tmpString;
            }

        }
        listKey = null;
    }

    public void makeTmpFile(ArrayList tmpBackupHeaderArr, ArrayList tmpBackupArr, String tmpFilename)
        throws IIPRequestException
    {
        FileWriter fw = null;
        BufferedWriter out = null;
        String tn = null;
        String tfn = tmpFilename;
        File tmpFile = new File(tmpPath + tfn);
        tmpFile.deleteOnExit();
        try
        {
            fw = new FileWriter(tmpFile);
            out = new BufferedWriter(fw);
            for(int i = 0; tmpBackupHeaderArr.size() > i; i++)
            {
                tn = tmpBackupHeaderArr.get(i) + "\n";
                out.write(tn);
            }

            for(int i = 0; tmpBackupArr.size() > i; i++)
            {
                tn = tmpBackupArr.get(i) + "\n";
                out.write(tn);
            }

            out.close();
            out = null;
        }
        catch(IOException ie)
        {
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException ioexception) { }
                out = null;
            }
            ie.printStackTrace();
            throw new IIPRequestException(ie.fillInStackTrace().toString());
        }
        finally
        {
            tmpFile = null;
        }
        return;
    }

    public void backupFile(String fileName)
        throws IIPRequestException
    {
        String filePath = plotPath + fileName;
        String newPath = backupPath + fileName;
        readPLFileInfo(filePath, fileName);
        copyFile(filePath, newPath);
        deleteFile(filePath);
    }

    public void readPLFileInfo(String filePath, String orderFile)
        throws IIPRequestException
    {
        String fileName = filePath;
        File plFile = new File(fileName);
        ArrayList plFileArray = new ArrayList();
        String tmpString = null;
        int count = 0;
        if(!plFile.canRead())
            break MISSING_BLOCK_LABEL_185;
        String line = null;
        BufferedReader in = null;
        FileReader fr = null;
        try
        {
            fr = new FileReader(plFile);
            in = new BufferedReader(fr);
            for(; (line = in.readLine()) != null && !line.equals(""); plFileArray.add(line.trim()));
            getPLFileInfo(plFileArray, orderFile);
            in.close();
            in = null;
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
            e.printStackTrace();
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        finally
        {
            plFile = null;
            plFileArray = null;
        }
        break MISSING_BLOCK_LABEL_194;
        System.err.println("[ERROR] Can not read file!!!");
    }

    public void getPLFileInfo(ArrayList PlInfo, String orderFile)
        throws IIPRequestException
    {
        ArrayList plFileArray = PlInfo;
        ArrayList tmpArray = null;
        ArrayList tmpArrayBB = new ArrayList();
        ArrayList tmpBackupArr = null;
        ArrayList tmpBackupHeaderArr = null;
        tmpArray = new ArrayList();
        tmpBackupArr = new ArrayList();
        tmpBackupHeaderArr = new ArrayList();
        StringTokenizer st1 = new StringTokenizer(orderFile, ".", false);
        String of1 = st1.nextToken();
        String of2 = st1.nextToken();
        String tmpFilename = null;
        String tmpString = null;
        HashMap tmpHeader = new HashMap();
        HashMap tmpMap = new HashMap();
        String OutputRequestNO = null;
        String infoString = null;
        int count = 0;
        int countAA = 0;
        String outputMdm = "Paper";
        for(Iterator listKeyAA = plFileArray.iterator(); listKeyAA.hasNext();)
        {
            tmpString = (String)listKeyAA.next();
            if(tmpString.startsWith("AA"))
                countAA++;
        }

        Iterator listKey;
        for(listKey = plFileArray.iterator(); listKey.hasNext(); tmpArray.clear())
        {
            tmpString = (String)listKey.next();
            List list = Utils.tokenizeMessageWithNoTrim(tmpString, ',');
            for(Iterator listKey2 = list.iterator(); listKey2.hasNext(); tmpArray.add(listKey2.next()));
            if(tmpString.startsWith("HD"))
            {
                tmpBackupHeaderArr.add(tmpString);
                tmpHeader.put("TotalNOofRecord", tmpArray.get(1));
                tmpHeader.put("TekiouTableNO", tmpArray.get(2));
                outputMdm = (String)tmpArray.get(3);
                tmpHeader.put("OutputMedium", outputMdm);
                if(outputMdm.equals("01"))
                    outputMdm = "\u7D19\u51FA\u529B";
                else
                if(outputMdm.equals("02"))
                    outputMdm = "CD-R\u51FA\u529B(Zuo)";
                else
                    outputMdm = "CD-R\u51FA\u529B(PDF)";
                tmpHeader.put("OutputRequestNO", tmpArray.get(4));
            } else
            if(tmpString.startsWith("S1"))
            {
                tmpBackupHeaderArr.add(tmpString);
                tmpHeader.put("DirectionNO", tmpArray.get(1));
                tmpHeader.put("EquipmentReferenceNO", tmpArray.get(2));
            } else
            if(tmpString.startsWith("S2"))
            {
                tmpBackupHeaderArr.add(tmpString);
                tmpHeader.put("DirectorName", tmpArray.get(1));
                tmpHeader.put("PauseCharacter", tmpArray.get(2));
                tmpHeader.put("OutputDirectionTime", tmpArray.get(3));
                tmpHeader.put("SupplierCode", tmpArray.get(4));
            } else
            if(tmpString.startsWith("S3"))
            {
                tmpBackupHeaderArr.add(tmpString);
                tmpHeader.put("A1", tmpArray.get(1));
                tmpHeader.put("A2", tmpArray.get(2));
                tmpHeader.put("A3", tmpArray.get(3));
                tmpHeader.put("A4", tmpArray.get(4));
                tmpHeader.put("OutputFormula", tmpArray.get(5));
            } else
            if(tmpString.startsWith("AA"))
            {
                if(count <= 0)
                {
                    count++;
                    tmpBackupArr.add(tmpString);
                    tmpMap = (HashMap)tmpHeader.clone();
                    tmpMap.put("DistributionPlaceRecord", tmpArray.get(1));
                    tmpMap.put("NOofOutput", tmpArray.get(2));
                    tmpMap.put("DrawingNO", tmpArray.get(3));
                } else
                {
                    if(countAA > 1)
                    {
                        OutputRequestNO = (String)tmpMap.get("OutputRequestNO") + "-" + count;
                        tmpFilename = of1 + "-" + count + "." + of2;
                    } else
                    {
                        OutputRequestNO = (String)tmpMap.get("OutputRequestNO");
                        tmpFilename = of1 + "." + of2;
                    }
                    tmpArrayBB = sort(tmpArrayBB);
                    tmpBackupArr.addAll(tmpArrayBB);
                    makeTmpFile(tmpBackupHeaderArr, tmpBackupArr, tmpFilename);
                    tmpBackupArr.clear();
                    tmpMap.put("BBInfo", tmpArrayBB);
                    tmpMap.put("orderName", tmpFilename);
                    realMap.put(OutputRequestNO, tmpMap);
                    infoString = OutputRequestNO + ":" + tmpMap.get("DirectorName") + ":" + tmpMap.get("NOofOutput") + ":" + tmpMap.get("DrawingNO") + ":" + outputMdm + ":";
                    responseFileInfo.add(infoString);
                    tmpArrayBB = null;
                    tmpArrayBB = new ArrayList();
                    count++;
                    tmpBackupArr.add(tmpString);
                    tmpMap = (HashMap)tmpHeader.clone();
                    tmpMap.put("DistributionPlaceRecord", tmpArray.get(1));
                    tmpMap.put("NOofOutput", tmpArray.get(2));
                    tmpMap.put("DrawingNO", tmpArray.get(3));
                }
            } else
            if(tmpString.startsWith("BB"))
                tmpArrayBB.add(tmpString);
            if(!listKey.hasNext())
            {
                if(countAA > 1)
                {
                    OutputRequestNO = (String)tmpMap.get("OutputRequestNO") + "-" + count;
                    tmpFilename = of1 + "-" + count + "." + of2;
                } else
                {
                    OutputRequestNO = (String)tmpMap.get("OutputRequestNO");
                    tmpFilename = of1 + "." + of2;
                }
                tmpArrayBB = sort(tmpArrayBB);
                tmpBackupArr.addAll(tmpArrayBB);
                makeTmpFile(tmpBackupHeaderArr, tmpBackupArr, tmpFilename);
                tmpMap.put("BBInfo", tmpArrayBB);
                tmpMap.put("orderName", tmpFilename);
                realMap.put(OutputRequestNO, tmpMap);
                infoString = OutputRequestNO + ":" + tmpMap.get("DirectorName") + ":" + tmpMap.get("NOofOutput") + ":" + tmpMap.get("DrawingNO") + ":" + outputMdm + ":";
                responseFileInfo.add(infoString);
                tmpArrayBB = null;
                tmpArrayBB = new ArrayList();
            }
        }

        plFileArray = null;
        tmpArray = null;
        tmpArrayBB = null;
        tmpHeader = null;
        tmpMap = null;
        listKey = null;
    }

    public boolean copyFile(String path, String newPath)
        throws IIPRequestException
    {
        File baseFile1;
        File baseFile2;
        BufferedInputStream in;
        BufferedOutputStream out;
        String localPath1 = path;
        if(Utils.isNullString(localPath1))
            return false;
        String localPath2 = newPath;
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

    public boolean deleteFile(String path)
        throws IIPRequestException
    {
        String localPath = path;
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

    public void deleteOrder(String command)
        throws IIPRequestException
    {
        List l = Utils.tokenizeMessage(command, ':');
        String delOrderNO = (String)l.get(1);
        HashMap tmpMap = (HashMap)realMap.get(delOrderNO);
        String delFileName = (String)tmpMap.get("orderName");
        realMap.remove(delOrderNO);
        boolean b = deleteFile(tmpPath + delFileName);
    }

    public synchronized ArrayList sort(ArrayList drawingList)
        throws IIPRequestException
    {
        String tmpString = null;
        ArrayList tempList2 = new ArrayList();
        ArrayList resultList = null;
        if(Utils.isNullArrayList(drawingList))
            return drawingList;
        for(int i = 0; drawingList.size() > i; i++)
        {
            tmpString = (String)drawingList.get(i);
            List l = Utils.tokenizeMessage(tmpString, ',');
            String dNO = (String)l.get(1);
            String ds = getDrawingSize(dNO);
            ArrayList tempList1 = new ArrayList();
            tempList1.add(tmpString);
            tempList1.add(ds);
            tempList2.add(tempList1);
            tempList1 = null;
        }

        Utils.sortArrayList(tempList2, 1, 0);
        resultList = new ArrayList();
        Iterator listKey;
        for(listKey = tempList2.iterator(); listKey.hasNext();)
        {
            ArrayList tempList1 = (ArrayList)listKey.next();
            if(tempList1 != null)
            {
                resultList.add(tempList1.get(0));
                tempList1 = null;
            }
        }

        listKey = null;
        tempList2.clear();
        tempList2 = null;
        return resultList;
    }

    public String getDrawingSize(String drawingNO)
        throws IIPRequestException
    {
        String drawingSize = null;
        DOSChangeable dc = null;
        ArrayList searchResults = null;
        HashMap searchCondition = new HashMap();
        searchCondition.put("version.condition.type", "wip");
        searchCondition.put("86056d04", drawingNO);
        try
        {
            dos.setWorkingModel("80001764");
            searchResults = dos.list("86056d01", searchCondition);
        }
        catch(Exception e)
        {
            System.out.println("Import_EC_ACTION.java : Drawing search error");
            e.printStackTrace();
        }
        if(Utils.isNullArrayList(searchResults))
            return null;
        try
        {
            String ouid = (String)((ArrayList)searchResults.get(0)).get(0);
            DOSChangeable obj = dos.get(ouid);
            if(obj == null)
                return null;
            String codeItemOuid = (String)obj.get("DrawingSize");
            if(Utils.isNullString(codeItemOuid))
                return null;
            DOSChangeable codeItem = dos.getCodeItem(codeItemOuid);
            if(codeItem == null)
                return null;
            drawingSize = (String)codeItem.get("codeitemid");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return drawingSize;
    }

    private void clearTempFolder()
    {
        String tempPath = null;
        File folder = null;
        File files[] = (File[])null;
        try
        {
            realMap.clear();
            tempPath = nds.getValue("::/RENESAS_SIRIUS_TEMP_FOLDER");
            if(Utils.isNullString(tempPath))
                return;
            folder = new File(tempPath);
            folder.mkdirs();
            if(!folder.isDirectory())
                return;
            files = folder.listFiles();
            if(files == null || files.length == 0)
                return;
            for(int i = 0; i < files.length; i++)
                try
                {
                    files[i].delete();
                }
                catch(Exception ee)
                {
                    ee.printStackTrace();
                }

            files = (File[])null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private DOS dos;
    private DTM dtm;
    private NDS nds;
    private DSS dss;
    private Server server;
    private ThreadPool tp;
    private DaemonLock dlock;
    private ArrayList jobs;
    private ArrayList plotList;
    private int port;
    private String portString;
    private String filePath;
    ServerSocket serverSocket;
    private final String CMD_REQUEST = "request";
    private final String CMD_PRINT = "print";
    private final String CMD_PSTOP = "pstop";
    private final String CMD_FREQUEST = "frequest";
    private final int IO_BUFFER_SIZE = 8192;
    private ArrayList printList;
    private HashMap seperatePLInfo;
    private HashMap realMap;
    private ArrayList responseFileInfo;
    private BundleDrawingIssue bdi;
    private String backupPath;
    private String plotPath;
    private String tmpPath;
    boolean main_flg;
    boolean printComplete;
    private String tmpOutputRequestNO;
    private boolean cpls;
    private int pls;
    private boolean cdr;
    private static final String BACK_PATH = "::/RENESAS_SIRIUS_BACKUP_FOLDER";
    private static final String PLOT_PATH = "::/RENESAS_SIRIUS_PLOT_FOLDER";
    private static final String TEMP_PATH = "::/RENESAS_SIRIUS_TEMP_FOLDER";
    private static final String PCS_PORT = "7959";
    private static final String EXIST_FILE = "FO";
    private static final String NOT_EXIST = "NN";
    private static final String PRINT_OK = "PO";
    private static final String PRINT_NOT_COMPLETE = "PN";
    private static final String CD_R = "CR";
}
