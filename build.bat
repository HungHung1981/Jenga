@echo off
setlocal
cd /d "%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo ERROR: javac was not found. Install a JDK and add its bin folder to PATH.
    exit /b 1
)
where jar >nul 2>nul
if errorlevel 1 (
    echo ERROR: jar was not found. Install a complete JDK and add its bin folder to PATH.
    exit /b 1
)
where jpackage >nul 2>nul
if errorlevel 1 (
    echo ERROR: jpackage was not found. Install a JDK that includes jpackage.
    exit /b 1
)

if not exist "build\classes" mkdir "build\classes"
if not exist "build\package" mkdir "build\package"
if not exist "dist" mkdir "dist"

echo Compiling Java sources...
javac -encoding UTF-8 -d "build\classes" *.java
if errorlevel 1 exit /b 1

echo Creating application JAR...
jar --create --file "build\Jenga.jar" --main-class Jenga -C "build\classes" .
if errorlevel 1 exit /b 1
copy /y "build\Jenga.jar" "build\package\Jenga.jar" >nul
if errorlevel 1 exit /b 1

if exist "dist\Jenga" rmdir /s /q "dist\Jenga"
echo Packaging Jenga.exe with its Java runtime...
jpackage --type app-image --name Jenga --input "build\package" --main-jar Jenga.jar --main-class Jenga --dest "dist" --win-console
if errorlevel 1 exit /b 1

echo.
echo Build complete: dist\Jenga\Jenga.exe
echo Share the complete dist\Jenga folder so the bundled runtime is included.
exit /b 0
