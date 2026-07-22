@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-24"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME=%JAVA_HOME%
java -version
echo.
echo Compilando SIFAC-SW...
call mvnw.cmd compile
