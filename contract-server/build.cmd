@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set Path=C:\Program Files\Java\jdk-17\bin;C:\Program Files\apache-maven-3.9.15\bin;%Path%
cd /d %~dp0
call mvn.cmd clean package -DskipTests
if %ERRORLEVEL% EQU 0 (
  echo Build SUCCESS
) else (
  echo Build FAILED
)
pause
