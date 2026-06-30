@echo off
if not exist "bin" mkdir bin
javac -cp "lib\sqlite-jdbc-3.40.0.0.jar" -d bin src\database\*.java src\model\*.java src\ui\*.java
java -cp "bin;lib\sqlite-jdbc-3.40.0.0.jar" ui.StudentResultSystem
pause
