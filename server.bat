@echo off

call cpset.bat

%JAVA_PATH%\bin\java.exe -Xms128m -Xmx512m -server -classpath %DYNAPATH%;D:\MyProjs\eclipse_workspace\DFEMPDM\dyna.jar dyna.framework.Server