// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Updater.java

package dyna.updater;

import com.jgoodies.animation.*;
import com.jgoodies.animation.animations.GlyphAnimation;
import com.jgoodies.animation.components.GlyphLabel;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DSS;
import dyna.framework.service.IDS;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.*;

public class Updater extends JDialog
    implements FileTransferCallback
{

    public Updater()
    {
        centerPanel = new JPanel();
        bottomPanel = new JPanel();
        labelPanel = new JPanel();
        progPanel = new JPanel();
        fileNameLabel = new JLabel();
        progLabel1 = new JLabel("File");
        progLabel2 = new JLabel("Download");
        progBar1 = new JProgressBar();
        progBar2 = new JProgressBar();
        glyphLabel = new GlyphLabel(" ", 5000L, 100L);
        animator = null;
        updateListFile = new File("update.dat");
        setTitle("DynaMOAD Update");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500, 200);
        setResizable(false);
        if(System.getProperty("java.specification.version").startsWith("1.4"))
            setUndecorated(true);
        Dimension frameSize = getSize();
        if(frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if(frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        labelPanel.setLayout(new BoxLayout(labelPanel, 1));
        progPanel.setLayout(new BoxLayout(progPanel, 1));
        progBar1.setPreferredSize(new Dimension(300, 18));
        progBar2.setPreferredSize(new Dimension(300, 18));
        progLabel1.setVerticalAlignment(0);
        progLabel2.setVerticalAlignment(0);
        progBar1.setStringPainted(true);
        progBar2.setStringPainted(true);
        cp.add(centerPanel, "Center");
        cp.add(bottomPanel, "South");
        bottomPanel.add(labelPanel, "West");
        bottomPanel.add(progPanel, "Center");
        labelPanel.add(progLabel1);
        labelPanel.add(progLabel2);
        progPanel.add(progBar1);
        progPanel.add(progBar2);
        progPanel.add(fileNameLabel, "South");
        update();
        closeDialog(null);
    }

    private void update()
    {
        ArrayList updateFileList = null;
        ArrayList updatedFileList = new ArrayList();
        try
        {
            updateFileList = ids.getUpdateFileList();
            if(updateFileList == null || updateFileList.isEmpty())
                closeDialog(null);
            if(!updateListFile.exists())
                updateListFile.createNewFile();
            String aLine = null;
            BufferedReader reader = new BufferedReader(new FileReader("update.dat"));
            do
            {
                aLine = reader.readLine();
                if(aLine == null)
                    break;
                updatedFileList.add(aLine);
            } while(true);
            reader.close();
            reader = null;
            Iterator fileKey;
            for(fileKey = updateFileList.iterator(); fileKey.hasNext();)
            {
                aLine = (String)fileKey.next();
                if(updatedFileList.contains(aLine))
                    fileKey.remove();
            }

            if(updateFileList.isEmpty())
                closeDialog(null);
            bulldAnimator();
            setVisible(true);
            progBar1.setMaximum(updateFileList.size() + 1);
            progBar1.setMinimum(0);
            progBar1.setValue(0);
            for(fileKey = updateFileList.iterator(); fileKey.hasNext();)
            {
                progBar1.setValue(progBar1.getValue() + 1);
                aLine = (String)fileKey.next();
                if(!updatedFileList.contains(aLine))
                {
                    download(aLine);
                    unjar(aLine);
                    delete(aLine);
                }
            }

            fileKey = null;
            progBar1.setValue(progBar1.getMaximum());
            BufferedWriter bw = new BufferedWriter(new FileWriter("update.dat", true));
            for(fileKey = updateFileList.iterator(); fileKey.hasNext(); bw.write(aLine))
            {
                aLine = (String)fileKey.next();
                bw.newLine();
            }

            fileKey = null;
            bw.flush();
            bw.close();
            bw = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void download(String serverPath)
        throws IIPRequestException
    {
        if(Utils.isNullString(serverPath))
        {
            return;
        } else
        {
            progBar2.setMinimum(0);
            progBar2.setMaximum((new Long(dss.getFileSize("/ids/" + serverPath))).intValue());
            progBar2.setValue(0);
            fileNameLabel.setText(serverPath);
            dss.downloadFileForce("/ids/" + serverPath, "tmp/" + serverPath, this);
            return;
        }
    }

    private void unjar(String fileName)
        throws IOException
    {
        if(Utils.isNullString(fileName))
            return;
        ZipInputStream inputStream = null;
        ZipEntry zipEntry = null;
        FileInputStream fis = new FileInputStream("tmp/" + fileName);
        if(fileName.endsWith(".jar"))
            inputStream = new JarInputStream(fis);
        else
        if(fileName.endsWith(".zip"))
            inputStream = new ZipInputStream(fis);
        else
            return;
        do
        {
            zipEntry = inputStream.getNextEntry();
            if(zipEntry != null)
            {
                fileNameLabel.setText(zipEntry.toString());
                createFile(inputStream, zipEntry.toString());
                zipEntry = null;
            } else
            {
                inputStream.close();
                inputStream = null;
                return;
            }
        } while(true);
    }

    private void createFile(ZipInputStream inputStream, String outputFileName)
    {
        if(inputStream == null || Utils.isNullString(outputFileName))
            return;
        BufferedOutputStream out = null;
        File file = new File(outputFileName);
        byte buffer[] = (byte[])null;
        try
        {
            if(outputFileName.endsWith("/") || outputFileName.endsWith("\\"))
            {
                if(!file.exists())
                    file.mkdirs();
                return;
            }
            if(!file.exists())
                file.createNewFile();
            if(!file.canWrite())
                return;
            buffer = new byte[8192];
            int inBytes = 0;
            boolean finished = false;
            out = new BufferedOutputStream(new FileOutputStream(file), 8192);
            do
            {
                inBytes = inputStream.read(buffer, 0, 8192);
                if(inBytes > 0)
                    out.write(buffer, 0, inBytes);
                else
                    finished = true;
            } while(!finished);
            out.flush();
            out.close();
            out = null;
            buffer = (byte[])null;
        }
        catch(IOException e)
        {
            buffer = (byte[])null;
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException ioexception) { }
                out = null;
            }
            e.printStackTrace();
        }
    }

    private void delete(String fileName)
    {
        if(Utils.isNullString(fileName))
        {
            return;
        } else
        {
            File file = new File("tmp/" + fileName);
            file.delete();
            file = null;
            return;
        }
    }

    private void bulldAnimator()
    {
        glyphLabel.setPreferredSize(new Dimension(450, 100));
        glyphLabel.setFont(new Font("Dialog", 3, 40));
        centerPanel.add(glyphLabel);
        animator = new Animator(createAnimation(), 30);
        animator.start();
    }

    private Animation createAnimation()
    {
        Animation message = new GlyphAnimation(glyphLabel, 5000L, 1L, "DynaMOAD Update");
        Animation all = Animations.sequential(new Animation[] {
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message, 
            message, message, message, message, message, message, message, message, message, message
        });
        return all;
    }

    private void closeDialog(WindowEvent evt)
    {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    public void transferBytes(int bytes)
    {
        progBar2.setValue(progBar2.getValue() + bytes);
    }

    public static void main(String args[])
    {
        try
        {
            dfw = new Client();
            dss = (DSS)dfw.getServiceInstance("DF30DSS1");
            ids = (IDS)dfw.getServiceInstance("DF30IDS1");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        new Updater();
    }

    JPanel centerPanel;
    JPanel bottomPanel;
    JPanel labelPanel;
    JPanel progPanel;
    JLabel fileNameLabel;
    JLabel progLabel1;
    JLabel progLabel2;
    JProgressBar progBar1;
    JProgressBar progBar2;
    private GlyphLabel glyphLabel;
    private Animator animator;
    private static Client dfw = null;
    private static DSS dss = null;
    private static IDS ids = null;
    private File updateListFile;

}
