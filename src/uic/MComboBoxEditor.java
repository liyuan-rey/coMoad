// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MComboBoxEditor.java

package dyna.uic;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxEditor;

class MComboBoxEditor extends BasicComboBoxEditor
{
    public static final class UIResource extends MComboBoxEditor
        implements javax.swing.plaf.UIResource
    {

        public UIResource()
        {
        }
    }


    public MComboBoxEditor()
    {
        editor = new JTextField("", 9);
        editor.setBorder(UIManager.getBorder("ComboBox.editorBorder"));
    }
}
