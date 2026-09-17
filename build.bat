@echo off
echo ===================================================
echo   Compiling Vityarthi Student Management System
echo ===================================================

if not exist "bin" mkdir bin
if not exist "data" mkdir data

javac -d bin -sourcepath src src\com\vityarthi\sms\Main.java src\com\vityarthi\sms\model\*.java src\com\vityarthi\sms\service\*.java src\com\vityarthi\sms\ui\*.java src\com\vityarthi\sms\util\*.java src\com\vityarthi\sms\exception\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Compilation successful! Class files generated in bin\ directory.
) else (
    echo.
    echo [ERROR] Compilation failed. Please inspect error messages above.
)
