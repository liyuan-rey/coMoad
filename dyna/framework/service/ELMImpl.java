// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ELMImpl.java

package dyna.framework.service;

import dyna.framework.Server;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.server.*;
import dyna.util.Utils;
import java.io.PrintStream;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Referenced classes of package dyna.framework.service:
//            ELM

public class ELMImpl extends ServiceServer
    implements ELM
{

    public ELMImpl()
    {
        mailServer = null;
        mailServer = Server.mailServer;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
    }

    public void sendMail(String from, ArrayList toList, String subject, String message)
        throws IIPRequestException
    {
        if(Utils.isNullString(from))
            throw new IIPRequestException("Miss out mandatory parameter(s) : from");
        if(Utils.isNullArrayList(toList))
            throw new IIPRequestException("Miss out mandatory parameter(s) : toList");
        if(Utils.isNullString(subject))
            throw new IIPRequestException("Miss out mandatory parameter(s) : subject");
        if(Utils.isNullString(message))
            throw new IIPRequestException("Miss out mandatory parameter(s) : messasge");
        boolean debug = true;
        Properties props = new Properties();
        props.put("mail.smtp.host", mailServer);
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        try
        {
            MimeMessage msg = new MimeMessage(session);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(from));
            InternetAddress address[] = new InternetAddress[toList.size()];
            for(int i = 0; i < toList.size(); i++)
                address[i] = new InternetAddress((String)toList.get(i));

            msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
            msg.setText(message);
            Transport.send(msg);
        }
        catch(MessagingException e)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
    }

    private String mailServer;
}
