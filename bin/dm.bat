@echo off

call cpset.bat

%JAVA_PATH%\bin\javaw.exe -classpath %DYNAPATH%;D:\MyProjs\eclipse_workspace\DFEMPDM\dyna.jar dyna.updater.Updater

%JAVA_PATH%\bin\javaw.exe -classpath %DYNAPATH%;D:\MyProjs\eclipse_workspace\DFEMPDM\dyna.jar dyna.framework.client.DynaMOAD