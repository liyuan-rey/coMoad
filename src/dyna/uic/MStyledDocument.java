// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MStyledDocument.java

package dyna.uic;

import dyna.util.Utils;
import java.awt.Toolkit;
import javax.swing.text.*;

public class MStyledDocument extends DefaultStyledDocument
{

    public MStyledDocument(double size, byte dataType)
    {
        this.size = 0.0D;
        this.dataType = 0;
        this.size = size;
        this.dataType = dataType;
    }

    public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException
    {
        if(str == null)
            return;
        if(size > 0.0D)
        {
            String tempString = null;
            if(getLength() > 0)
            {
                tempString = getText(0, getLength());
                if(tempString != null)
                {
                    if(offs == 0)
                        tempString = str + tempString;
                    else
                        tempString = tempString.substring(0, offs) + str + tempString.substring(offs);
                } else
                {
                    tempString = str;
                }
            } else
            {
                tempString = str;
            }
            if(tempString == null)
                return;
            switch(dataType)
            {
            case 2: // '\002'
            case 6: // '\006'
            case 7: // '\007'
            case 8: // '\b'
                if("01234567890-".indexOf(tempString.charAt(0)) < 0)
                {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                for(int i = 1; i < tempString.length(); i++)
                    if("01234567890".indexOf(tempString.charAt(i)) < 0)
                    {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }

                break;

            case 4: // '\004'
            case 5: // '\005'
                if("01234567890-.".indexOf(tempString.charAt(0)) < 0)
                {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                if(tempString.indexOf('.') >= 0 && tempString.indexOf('.') < tempString.length() - 1 && tempString.indexOf('.', tempString.indexOf('.') + 1) >= 0)
                {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                for(int i = 1; i < tempString.length(); i++)
                    if("01234567890.".indexOf(tempString.charAt(i)) < 0)
                    {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }

                break;
            }
            if(!Utils.checkStringSize(tempString, size, dataType))
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }
        insertString(offs, str, a);
    }

    double size;
    byte dataType;
}
