// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-9 0:40:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DynaMOAD.java

package dyna.framework.client;

import com.jgoodies.plaf.FontUtils;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.plaf.plastic.theme.ExperienceBlue;

import dyna.framework.Client;
import dyna.framework.service.ACL;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.MSR;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.WKS;
import dyna.framework.service.msr.MSRStgrep;
import dyna.uic.DynaTheme;
import dyna.uic.ExceptionPrintStream;
import dyna.uic.SplashWindow;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

// Referenced classes of package dyna.framework.client:
//            LogIn, UIManagement, SearchCondition, SearchCondition4CADIntegration, 
//            SearchResult, SearchResult4CADIntegration

public class DynaMOAD
{

    public DynaMOAD()
    {
        init();
        javax.swing.UIDefaults uiDefaults = UIManager.getDefaults();
        if(!isDeamonMode)
        {
            LogIn login = new LogIn();
            login.setVisible(true);
        }
    }

    public DynaMOAD(String userId, String password)
    {
        init();
        javax.swing.UIDefaults uiDefaults = UIManager.getDefaults();
        LogIn logIn = new LogIn();
        logIn.userIDTextField.setText(userId);
        logIn.passwordTextField.setText(password);
        logIn.OKEvent();
    }

    private void init()
    {
        try
        {
            LookUtils.setLookAndTheme(new PlasticXPLookAndFeel(), new ExperienceBlue());
            FontUtils.initFontDefaults(UIManager.getLookAndFeelDefaults(), new Font("dialog", 0, 12), new Font("dialog", 1, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12));
            dfw = new Client();
            dos = (DOS)dfw.getServiceInstance("DF30DOS1");
            nds = (NDS)dfw.getServiceInstance("DF30NDS1");
            dtm = (DTM)dfw.getServiceInstance("DF30DTM1");
            aus = (AUS)dfw.getServiceInstance("DF30AUS1");
            dss = (DSS)dfw.getServiceInstance("DF30DSS1");
            msr = (MSR)dfw.getServiceInstance("DF30MSR1");
            wfm = (WFM)dfw.getServiceInstance("DF30WFM1");
            acl = (ACL)dfw.getServiceInstance("DF30ACL1");
            wks = (WKS)dfw.getServiceInstance("DF30WKS1");
            reservedFields = new LinkedList();
            reservedFields.add("md$number");
            reservedFields.add("md$desc");
            reservedFields.add("md$description");
            reservedFields.add("md$status");
            reservedFields.add("md$cdate");
            reservedFields.add("md$mdate");
            reservedFields.add("md$user");
            reservedFields.add("vf$version");
            System.setErr(new ExceptionPrintStream(System.err));
            if(!dfw.getLicense("DP-BASE"))
            {
                JOptionPane.showMessageDialog(null, "LICENSE UNAVAILABLE.", "Error Message", 0);
                System.exit(998);
            }
            msr.setCurrentLocale(Client.locale);
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public static void main(String args[])
    {
        String userId = null;
        String password = null;
        if(args.length > 0)
        {
            parameters = new ArrayList(args.length);
            for(int i = 0; i < args.length; i++)
                parameters.add(args[i]);

            if(args[0].equals("daemonMode"))
                isDeamonMode = true;
            else
                isDeamonMode = false;
            if(args.length > 2)
            {
                userId = args[1];
                password = args[2];
            }
        }
        if(userId != null && password != null)
            new DynaMOAD(userId, password);
        else
            new DynaMOAD();
    }

    public static JPanel buildStatusBar(JLabel statusField)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(statusField, "West");
        statusField.setBorder(BorderFactory.createEmptyBorder(0, 6, 2, 3));
        statusField.setFont(new Font("Dialog", 2, 11));
        return panel;
    }

    public static ImageIcon getMOAD16Icon()
    {
        if(moad16icon == null)
            moad16icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif").getScaledInstance(16, 16, 16));
        return moad16icon;
    }

    public static String getMSRString(String id, String substitute, int opt)
    {
        String returnStr = null;
        String tmpStr = null;
        if(stringRepository == null)
            return substitute;
        MSRStgrep msrStgrep = (MSRStgrep)stringRepository.get(Client.locale + "_" + id);
        if(msrStgrep != null)
            tmpStr = msrStgrep.stg;
        else
            tmpStr = substitute;
        msrStgrep = null;
        if(tmpStr != null && tmpStr.length() > 0 && Client.locale.equals("en"))
            switch(opt)
            {
            case 0: // '\0'
                returnStr = tmpStr;
                // fall through

            case 1: // '\001'
                returnStr = tmpStr.toUpperCase();
                // fall through

            case 2: // '\002'
                returnStr = tmpStr.toLowerCase();
                // fall through

            case 3: // '\003'
                java.util.List tokens = Utils.tokenizeMessage(tmpStr, ' ');
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < tokens.size(); i++)
                {
                    tmpStr = (String)tokens.get(i);
                    sb.append(tmpStr.substring(0, 1).toUpperCase()).append(tmpStr.substring(1, tmpStr.length()));
                    if(i < tokens.size() - 1)
                        sb.append(" ");
                }

                returnStr = sb.toString();
                break;
            }
        else
            returnStr = tmpStr;
        return returnStr;
    }

    public static ArrayList getParameterArray()
    {
        return parameters;
    }

    public static void enableDaemonMode()
    {
        isDeamonMode = true;
    }

    public static final String COPYRIGHT_TEXT = "Copyright (c) 2004, EESIN Information Technology Ltd.";
    public static Client dfw = null;
    public static DOS dos = null;
    public static NDS nds = null;
    public static DTM dtm = null;
    public static AUS aus = null;
    public static DSS dss = null;
    public static MSR msr = null;
    public static WFM wfm = null;
    public static ACL acl = null;
    public static WKS wks = null;
    public static UIManagement newUI;
    public static HashMap stringRepository = null;
    public static SplashWindow splashWindow = null;
    public static SearchCondition searchCondition = null;
    public static SearchCondition4CADIntegration searchCondition4CADIntegration = null;
    public static SearchResult searchResult = null;
    public static SearchResult4CADIntegration searchResult4CADIntegration = null;
    public static boolean isDeamonMode = false;
    public static boolean startFromCAD = false;
    public static LinkedList reservedFields = null;
    private static ArrayList parameters = null;
    private static ImageIcon moad16icon = null;

}