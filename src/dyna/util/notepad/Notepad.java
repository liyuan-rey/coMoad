// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Notepad.java

package dyna.util.notepad;

import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.plaf.plastic.theme.ExperienceBlue;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.client.FileTransferDialog;
import dyna.framework.editor.modeler.ObjectModelingConstruction;
import dyna.framework.editor.workflow.WorkflowModeler;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DSS;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.*;

// Referenced classes of package dyna.util.notepad:
//            ElementTreePanel

public class Notepad extends JPanel
    implements KeyListener
{
    protected class AppCloser extends WindowAdapter
    {

        public void windowClosing(WindowEvent e)
        {
            Notepad.frame.dispose();
        }

        protected AppCloser()
        {
        }
    }

    private class ActionChangedListener
        implements PropertyChangeListener
    {

        public void propertyChange(PropertyChangeEvent e)
        {
            String propertyName = e.getPropertyName();
            if(e.getPropertyName().equals("Name"))
            {
                String text = (String)e.getNewValue();
                menuItem.setText(text);
            } else
            if(propertyName.equals("enabled"))
            {
                Boolean enabledState = (Boolean)e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            }
        }

        JMenuItem menuItem;

        ActionChangedListener(JMenuItem mi)
        {
            menuItem = mi;
        }
    }

    class UndoHandler
        implements UndoableEditListener
    {

        public void undoableEditHappened(UndoableEditEvent e)
        {
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }

        UndoHandler()
        {
        }
    }

    class StatusBar extends JComponent
    {

        public void paint(Graphics g)
        {
            super.paint(g);
        }

        public StatusBar()
        {
            setLayout(new BoxLayout(this, 0));
        }
    }

    class UndoAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                undo.undo();
            }
            catch(CannotUndoException ex)
            {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update()
        {
            if(undo.canUndo())
            {
                saveButton.setEnabled(true);
                setEnabled(true);
                putValue("Name", undo.getUndoPresentationName());
            } else
            {
                saveButton.doClick();
                saveButton.setEnabled(false);
                setEnabled(false);
                putValue("Name", "Undo");
            }
        }

        public UndoAction()
        {
            super("Undo");
            setEnabled(false);
        }
    }

    class RedoAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                undo.redo();
            }
            catch(CannotRedoException ex)
            {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            saveButton.setEnabled(true);
            undoAction.update();
        }

        protected void update()
        {
            if(undo.canRedo())
            {
                setEnabled(true);
                putValue("Name", undo.getRedoPresentationName());
            } else
            {
                setEnabled(false);
                putValue("Name", "Redo");
            }
        }

        public RedoAction()
        {
            super("Redo");
            setEnabled(false);
        }
    }

    class SaveAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                if(openFile != null)
                {
                    FileWriter modifiedWriter = new FileWriter(openFile);
                    modifiedWriter.write(getEditor().getText());
                    modifiedWriter.flush();
                } else
                {
                    if(fileDialog == null)
                        fileDialog = new FileDialog(Notepad.frame);
                    fileDialog.setMode(1);
                    fileDialog.show();
                    String file = fileDialog.getDirectory() + fileDialog.getFile();
                    if(fileDialog.getDirectory() == null || fileDialog.getFile() == null)
                        return;
                    FileWriter modifiedWriter = new FileWriter(file);
                    modifiedWriter.write(getEditor().getText());
                    modifiedWriter.flush();
                    openFile = new File(file);
                    Notepad.frame.setTitle(openFile.getName());
                }
                saveButton.setEnabled(false);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }

        SaveAction()
        {
            super("save");
        }
    }

    class FileCallBack
        implements FileTransferCallback
    {

        public void transferBytes(int bytes)
        {
            ftd.addSize(bytes);
        }

        FileCallBack()
        {
        }
    }

    class UploadAction extends AbstractAction
    {

        public void upload(String serverPath, String clientPath, FileTransferCallback callback)
        {
            _serverPath = serverPath;
            _clientPath = clientPath;
            _ftc = callback;
            SwingWorker worker = new SwingWorker() {

                public Object construct()
                {
                    try
                    {
                        dss.uploadFile(serverPath, clientPath, ftc);
                    }
                    catch(IIPRequestException e)
                    {
                        System.err.println(e.getLocalizedMessage());
                    }
                    return null;
                }

                public void finished()
                {
                    ftd.setVisible(false);
                    ftd.dispose();
                }

                String serverPath;
                String clientPath;
                FileTransferCallback ftc;

                
                {
                    serverPath = _serverPath;
                    clientPath = _clientPath;
                    ftc = _ftc;
                }
            };
            worker.start();
        }

        public void actionPerformed(ActionEvent e)
        {
            if(openFile == null)
            {
                JOptionPane.showMessageDialog(Notepad.frame, "\uD30C\uC77C\uC774 \uC5C6\uC2B5\uB2C8\uB2E4.", "Error", 0);
                return;
            }
            Object option[] = {
                "\uC608", "\uC544\uB2C8\uC624"
            };
            int res = JOptionPane.showOptionDialog(Notepad.frame, "Upload \uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "Upload to server", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[0]);
            if(res != 0)
                return;
            if(ftd == null)
                ftd = new FileTransferDialog(Notepad.frame, false);
            ftd.setMaximumSize((new Long(openFile.length())).intValue());
            UIUtils.setLocationRelativeTo(ftd, Notepad.frame);
            ftd.setVisible(true);
            FileCallBack fileUp = new FileCallBack();
            upload("/script/" + openFile.getName(), openFile.getAbsolutePath(), fileUp);
        }


        UploadAction()
        {
            super("upload");
        }
    }

    class OpenAction extends NewAction
    {

        public void actionPerformed(ActionEvent e)
        {
            Frame frame = getFrame();
            if(fileDialog == null)
                fileDialog = new FileDialog(frame);
            fileDialog.setMode(0);
            fileDialog.show();
            String file = fileDialog.getFile();
            if(file == null)
                return;
            String directory = fileDialog.getDirectory();
            openFile = new File(directory, file);
            if(openFile.exists())
            {
                Document oldDoc = getEditor().getDocument();
                if(oldDoc != null)
                    oldDoc.removeUndoableEditListener(undoHandler);
                if(elementTreePanel != null)
                    elementTreePanel.setEditor(null);
                getEditor().setDocument(new PlainDocument());
                frame.setTitle(file);
                Thread loader = new FileLoader(openFile, editor.getDocument());
                loader.start();
            }
        }

        OpenAction()
        {
            super("open");
        }
    }

    class NewAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            Document oldDoc = getEditor().getDocument();
            if(oldDoc != null)
                oldDoc.removeUndoableEditListener(undoHandler);
            getEditor().setDocument(new PlainDocument());
            getEditor().getDocument().addUndoableEditListener(undoHandler);
            resetUndoManager();
            revalidate();
            openFile = null;
            Notepad.frame.setTitle("Notepad");
        }

        NewAction()
        {
            super("new");
        }

        NewAction(String nm)
        {
            super(nm);
        }
    }

    class ExitAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            Notepad.frame.dispose();
        }

        ExitAction()
        {
            super("exit");
        }
    }

    class ShowElementTreeAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            if(elementTreeFrame == null)
            {
                try
                {
                    String title = Notepad.resources.getString("ElementTreeFrameTitle");
                    elementTreeFrame = new JFrame(title);
                }
                catch(MissingResourceException mre)
                {
                    elementTreeFrame = new JFrame();
                }
                elementTreeFrame.addWindowListener(new WindowAdapter() {

                    public void windowClosing(WindowEvent weeee)
                    {
                        elementTreeFrame.setVisible(false);
                    }

                });
                Container fContentPane = elementTreeFrame.getContentPane();
                fContentPane.setLayout(new BorderLayout());
                elementTreePanel = new ElementTreePanel(getEditor());
                fContentPane.add(elementTreePanel);
                elementTreeFrame.pack();
            }
            elementTreeFrame.show();
        }


        ShowElementTreeAction()
        {
            super("showElementTree");
        }

        ShowElementTreeAction(String nm)
        {
            super(nm);
        }
    }

    class FileLoader extends Thread
    {

        public void run()
        {
            try
            {
                status.removeAll();
                JProgressBar progress = new JProgressBar();
                progress.setMinimum(0);
                progress.setMaximum((int)f.length());
                status.add(progress);
                status.revalidate();
                Reader in = new FileReader(f);
                char buff[] = new char[4096];
                int nch;
                while((nch = in.read(buff, 0, buff.length)) != -1) 
                {
                    doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
                    progress.setValue(progress.getValue() + nch);
                }
                doc.addUndoableEditListener(undoHandler);
                status.removeAll();
                status.revalidate();
                resetUndoManager();
            }
            catch(IOException e)
            {
                System.err.println(e.toString());
            }
            catch(BadLocationException e)
            {
                System.err.println(e.getMessage());
            }
            if(elementTreePanel != null)
                SwingUtilities.invokeLater(new Runnable() {

                    public void run()
                    {
                        elementTreePanel.setEditor(getEditor());
                    }

                });
        }

        Document doc;
        File f;


        FileLoader(File f, Document doc)
        {
            setPriority(4);
            this.f = f;
            this.doc = doc;
        }
    }


    Notepad()
    {
        super(true);
        ftd = null;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        undoHandler = new UndoHandler();
        undo = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        defaultActions = (new Action[] {
            new NewAction(), new OpenAction(), new SaveAction(), new UploadAction(), new ExitAction(), new ShowElementTreeAction(), undoAction, redoAction
        });
        try
        {
            LookUtils.setLookAndTheme(new PlasticXPLookAndFeel(), new ExperienceBlue());
            FontUtils.initFontDefaults(UIManager.getLookAndFeelDefaults(), new Font("dialog", 0, 12), new Font("dialog", 1, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 11), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12));
        }
        catch(Exception exc)
        {
            System.err.println("Error loading L&F: " + exc);
        }
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        editor = createEditor();
        editor.setFont(new Font("monospaced", 0, 12));
        editor.getDocument().addUndoableEditListener(undoHandler);
        editor.addKeyListener(this);
        commands = new Hashtable();
        Action actions[] = getActions();
        for(int i = 0; i < actions.length; i++)
        {
            Action a = actions[i];
            commands.put(a.getValue("Name"), a);
        }

        JScrollPane scroller = UIFactory.createStrippedScrollPane(null);
        JViewport port = scroller.getViewport();
        port.add(editor);
        try
        {
            String vpFlag = resources.getString("ViewportBackingStore");
            Boolean bs = new Boolean(vpFlag);
            port.setBackingStoreEnabled(bs.booleanValue());
        }
        catch(MissingResourceException missingresourceexception) { }
        menuItems = new Hashtable();
        menubar = createMenubar();
        add("North", menubar);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add("North", createToolbar());
        panel.add("Center", scroller);
        add("Center", panel);
        add("South", createStatusbar());
    }

    public Notepad(File scriptFile)
    {
        this();
        try
        {
            if(ObjectModelingConstruction.dfw != null)
                dss = (DSS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DSS1");
            else
            if(WorkflowModeler.dss != null)
                dss = WorkflowModeler.dss;
        }
        catch(Exception ie)
        {
            ie.printStackTrace();
        }
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icons/dynapdm.jpg"));
        frame.setTitle(resources.getString("Title"));
        frame.setBackground(Color.lightGray);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("Center", this);
        frame.addWindowListener(new AppCloser());
        frame.pack();
        frame.setSize(500, 600);
        frame.show();
        if(scriptFile == null)
            return;
        openFile = scriptFile.getAbsoluteFile();
        if(openFile.exists())
        {
            Document oldDoc = getEditor().getDocument();
            if(oldDoc != null)
                oldDoc.removeUndoableEditListener(undoHandler);
            if(elementTreePanel != null)
                elementTreePanel.setEditor(null);
            getEditor().setDocument(new PlainDocument());
            frame.setTitle(scriptFile.getName());
            Thread loader = new FileLoader(openFile, editor.getDocument());
            loader.start();
        }
    }

    public Action[] getActions()
    {
        return TextAction.augmentList(editor.getActions(), defaultActions);
    }

    protected JTextComponent createEditor()
    {
        return new JTextArea();
    }

    protected JTextComponent getEditor()
    {
        return editor;
    }

    protected Frame getFrame()
    {
        for(Container p = getParent(); p != null; p = p.getParent())
            if(p instanceof Frame)
                return (Frame)p;

        return null;
    }

    protected JMenuItem createMenuItem(String cmd)
    {
        JMenuItem mi = new JMenuItem(getResourceString(cmd + "Label"));
        URL url = getResource(cmd + "Image");
        if(url != null)
        {
            mi.setHorizontalTextPosition(4);
            mi.setIcon(new ImageIcon(url));
        }
        String astr = getResourceString(cmd + "Action");
        if(astr == null)
            astr = cmd;
        mi.setActionCommand(astr);
        Action a = getAction(astr);
        if(a != null)
        {
            mi.addActionListener(a);
            a.addPropertyChangeListener(createActionChangeListener(mi));
            mi.setEnabled(a.isEnabled());
        } else
        {
            mi.setEnabled(false);
        }
        menuItems.put(cmd, mi);
        return mi;
    }

    protected JMenuItem getMenuItem(String cmd)
    {
        return (JMenuItem)menuItems.get(cmd);
    }

    protected Action getAction(String cmd)
    {
        return (Action)commands.get(cmd);
    }

    protected String getResourceString(String nm)
    {
        String str;
        try
        {
            str = resources.getString(nm);
        }
        catch(MissingResourceException mre)
        {
            str = null;
        }
        return str;
    }

    protected URL getResource(String key)
    {
        String name = getResourceString(key);
        if(name != null)
        {
            URL url = getClass().getResource(name);
            return url;
        } else
        {
            return null;
        }
    }

    protected Container getToolbar()
    {
        return toolbar;
    }

    protected JMenuBar getMenubar()
    {
        return menubar;
    }

    protected Component createStatusbar()
    {
        status = new StatusBar();
        return status;
    }

    protected void resetUndoManager()
    {
        undo.discardAllEdits();
        undoAction.update();
        redoAction.update();
    }

    private Component createToolbar()
    {
        toolbar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
        toolbar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        String toolKeys[] = tokenize(getResourceString("toolbar"));
        for(int i = 0; i < toolKeys.length; i++)
            if(toolKeys[i].equals("-"))
            {
                toolbar.add(Box.createHorizontalStrut(5));
            } else
            {
                toolKeys[i].equals("save");
                toolbar.add(createTool(toolKeys[i]));
            }

        return toolbar;
    }

    protected Component createTool(String key)
    {
        return createToolbarButton(key);
    }

    protected JButton createToolbarButton(String key)
    {
        URL url = getResource(key + "Image");
        JButton b = new ToolBarButton(new ImageIcon(url));
        String astr = getResourceString(key + "Action");
        if(astr == null)
            astr = key;
        Action a = getAction(astr);
        if(a != null)
        {
            b.setActionCommand(astr);
            b.addActionListener(a);
            if(astr.equals("save"))
            {
                saveButton = b;
                b.setEnabled(false);
            }
        } else
        {
            b.setEnabled(false);
        }
        String tip = getResourceString(key + "Tooltip");
        if(tip != null)
            b.setToolTipText(tip);
        return b;
    }

    protected String[] tokenize(String input)
    {
        Vector v = new Vector();
        for(StringTokenizer t = new StringTokenizer(input); t.hasMoreTokens(); v.addElement(t.nextToken()));
        String cmd[] = new String[v.size()];
        for(int i = 0; i < cmd.length; i++)
            cmd[i] = (String)v.elementAt(i);

        return cmd;
    }

    protected JMenuBar createMenubar()
    {
        JMenuBar mb = new JMenuBar();
        mb.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        String menuKeys[] = tokenize(getResourceString("menubar"));
        for(int i = 0; i < menuKeys.length; i++)
        {
            JMenu m = createMenu(menuKeys[i]);
            if(m != null)
                mb.add(m);
        }

        return mb;
    }

    protected JMenu createMenu(String key)
    {
        String itemKeys[] = tokenize(getResourceString(key));
        JMenu menu = new JMenu(getResourceString(key + "Label"));
        for(int i = 0; i < itemKeys.length; i++)
            if(itemKeys[i].equals("-"))
            {
                menu.add(new JSeparator());
            } else
            {
                JMenuItem mi = createMenuItem(itemKeys[i]);
                if(itemKeys[i].equals("undo"))
                    mi.setAccelerator(KeyStroke.getKeyStroke(90, 2));
                else
                if(itemKeys[i].equals("redo"))
                    mi.setAccelerator(KeyStroke.getKeyStroke(89, 2));
                else
                if(itemKeys[i].equals("save"))
                    mi.setAccelerator(KeyStroke.getKeyStroke(83, 2));
                menu.add(mi);
            }

        return menu;
    }

    protected PropertyChangeListener createActionChangeListener(JMenuItem b)
    {
        return new ActionChangedListener(b);
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyPressed(KeyEvent e)
    {
        if(saveButton != null && !e.isControlDown())
            saveButton.setEnabled(true);
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    private static JFrame frame;
    private static ResourceBundle resources;
    private File openFile;
    private DSS dss;
    private JTextComponent editor;
    private Hashtable commands;
    private Hashtable menuItems;
    private JMenuBar menubar;
    private JToolBar toolbar;
    private JComponent status;
    private JFrame elementTreeFrame;
    protected ElementTreePanel elementTreePanel;
    protected FileDialog fileDialog;
    private FileTransferDialog ftd;
    private String _serverPath;
    private String _clientPath;
    private FileTransferCallback _ftc;
    private JButton saveButton;
    protected UndoableEditListener undoHandler;
    protected UndoManager undo;
    public static final String imageSuffix = "Image";
    public static final String labelSuffix = "Label";
    public static final String actionSuffix = "Action";
    public static final String tipSuffix = "Tooltip";
    public static final String openAction = "open";
    public static final String newAction = "new";
    public static final String saveAction = "save";
    public static final String uploadAction = "upload";
    public static final String exitAction = "exit";
    public static final String showElementTreeAction = "showElementTree";
    private UndoAction undoAction;
    private RedoAction redoAction;
    private Action defaultActions[];

    static 
    {
        try
        {
            resources = ResourceBundle.getBundle("dyna.util.notepad.resources.Notepad", Locale.getDefault());
        }
        catch(MissingResourceException mre)
        {
            System.err.println("dyna.util.notepad.resources/Notepad.properties not found");
        }
    }




















}
