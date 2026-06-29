@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=C:\Program Files\Java\jdk-17\bin;C:\Program Files\apache-maven-3.9.15\bin;%PATH%
cd /d C:\PROPOJET\合同管理\contract-server
echo Building contract-server with Java 17...
"C:\Program Files\Java\jdk-17\bin\java" -version
echo.
call "C:\Program Files\apache-maven-3.9.15\bin\mvn.cmd" clean package -DskipTests %*
