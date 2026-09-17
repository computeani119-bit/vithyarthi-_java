@echo off
if not exist "bin\com\vityarthi\sms\Main.class" (
    echo Building project first...
    call build.bat
)

echo Starting Vityarthi Student Management System...
java -cp bin com.vityarthi.sms.Main %*
