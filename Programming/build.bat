@echo off
setlocal EnableDelayedExpansion
echo =================================
echo Building AIMS Project
echo =================================
echo.

REM Clean and create directories
echo Cleaning up previous build...
rmdir /s /q build 2>nul
if exist "aimsfinal" (
    for /r "aimsfinal" %%i in (*.class) do del /f /q "%%i"
)
mkdir "build\classes"
mkdir "build\jar"
echo.

echo Step 1: Building Desktop Application...
javac -d build\classes -cp "aimsfinal\lib\*" aimsfinal\*.java aimsfinal\Model\*.java aimsfinal\DAO\*.java aimsfinal\Util\*.java aimsfinal\Interface\*.java aimsfinal\Controller\*.java aimsfinal\GUI\*.java aimsfinal\GUI\component\ButtonUI\*.java aimsfinal\GUI\component\CustomTable\*.java aimsfinal\GUI\component\ButtonAction\*.java aimsfinal\GUI\dialog\*.java aimsfinal\GUI\dialog\auth\*.java aimsfinal\GUI\dialog\addDialog\*.java aimsfinal\GUI\dialog\editDialog\*.java aimsfinal\GUI\dialog\viewDialog\*.java
if %errorlevel% neq 0 (
    echo ERROR: Desktop build failed.
    pause
    exit /b 1
)
echo Desktop build completed successfully.
echo.

echo Step 2: Preparing JAR resources (Manifest and Libs)...
REM Copy libraries to a 'lib' folder next to the JAR
mkdir "build\jar\lib"
copy "aimsfinal\lib\*.jar" "build\jar\lib\" > nul

REM Create the Class-Path string for the manifest
set "CP="
for %%j in (build\jar\lib\*.jar) do (
    set "CP=!CP! lib/%%~nxj"
)

REM Create the manifest file
(
    echo Main-Class: aimsfinal.GUI.MainFrame
    echo Class-Path:!CP!
) > build\manifest.txt
echo Manifest created.
echo.

echo Step 3: Creating executable JAR file...
jar -cfm build\jar\aims-swing.jar build\manifest.txt -C build\classes .
if %errorlevel% neq 0 (
    echo ERROR: JAR creation failed.
    pause
    exit /b 1
)
echo JAR file created successfully.
echo.

del build\manifest.txt

echo =================================
echo Build completed successfully!
echo =================================
echo.
echo Generated distributable in: build\jar\
echo To run, open a terminal in 'build\jar' and type: java -jar aims-swing.jar
echo.
pause