@echo off

call cpset.bat

%JAVA_PATH%\bin\java.exe -classpath %DYNAPATH%;D:\MyProjs\eclipse_workspace\DFEMPDM\dyna.jar dyna.framework.editor.workflow.WorkflowModeler