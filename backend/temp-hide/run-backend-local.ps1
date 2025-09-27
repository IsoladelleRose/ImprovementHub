Set-Location "C:\Users\stijn\ImprovementHub\backend\temp-hide"
$env:JAVA_HOME = "C:\Program Files\Java\jdk-19"
$env:M2_HOME = "C:\Users\stijn\apache-maven-3.9.6"
$env:PATH = "$env:JAVA_HOME\bin;$env:M2_HOME\bin;$env:PATH"

Write-Host "JAVA_HOME: $env:JAVA_HOME"
Write-Host "M2_HOME: $env:M2_HOME"
Write-Host "Java version:"
java -version

Write-Host ""
Write-Host "Maven version:"
mvn -version

Write-Host ""
Write-Host "Starting Spring Boot application with Maven (LOCAL PROFILE)..."
mvn spring-boot:run "-Dspring-boot.run.profiles=local"