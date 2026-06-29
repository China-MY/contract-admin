$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:Path = "C:\Program Files\Java\jdk-17\bin;C:\Program Files\apache-maven-3.9.15\bin;$env:Path"
cd "C:\PROPOJET\合同管理\contract-server"
Write-Host "Using Java 17..."
& "$env:JAVA_HOME\bin\java" -version
Write-Host ""
& "C:\Program Files\apache-maven-3.9.15\bin\mvn.cmd" $args
