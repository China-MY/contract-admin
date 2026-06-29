@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=C:\Program Files\Java\jdk-17\bin;%PATH%
cd /d %~dp0
echo Using Java:
"%JAVA_HOME%\bin\java" -version
echo.
"C:\Program Files\apache-maven-3.9.15\bin\mvn.cmd" %*
