@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-19.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo JAVA_HOME: %JAVA_HOME%
echo Java version:
java -version

echo.
echo Starting Maven wrapper...
mvnw.cmd spring-boot:run