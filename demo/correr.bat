@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-24"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME=%JAVA_HOME%
echo.
echo Compilando SIFAC-SW...
call mvnw.cmd compile
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Falló la compilación. No se ejecutará la aplicación.
    exit /b 1
)
echo.
echo Ejecutando SIFAC-SW...
call mvnw.cmd javafx:run
