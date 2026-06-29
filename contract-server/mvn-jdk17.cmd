@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set JAVACMD=%JAVA_HOME%\bin\java.exe
set Path=%JAVA_HOME%\bin;%Path%
cd /d C:\PROPOJET\合同管理\contract-server
echo Using Java 17...
"%JAVACMD%" -version
echo.
call "C:\Program Files\apache-maven-3.9.15\bin\mvn.cmd" %*
