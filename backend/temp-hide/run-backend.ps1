Set-Location "C:\Users\stijn\ImprovementHub\backend\temp-hide"
$env:JAVA_HOME = "C:\Program Files\Java\jdk-19"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "JAVA_HOME: $env:JAVA_HOME"
Write-Host "Java version:"
java -version

Write-Host ""
Write-Host "Starting Maven wrapper..."
.\mvnw.cmd spring-boot:run