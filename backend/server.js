const { spawn } = require('child_process');
const fs = require('fs');
const path = require('path');

console.log('Starting Java Spring Boot application...');

// Find the JAR file
const targetDir = path.join(__dirname, 'target');
let jarFile = null;

if (fs.existsSync(targetDir)) {
    const files = fs.readdirSync(targetDir);
    jarFile = files.find(file => file.endsWith('.jar') && !file.includes('original'));
}

if (!jarFile) {
    console.error('No JAR file found! Please run build first.');
    process.exit(1);
}

const jarPath = path.join(targetDir, jarFile);
console.log(`Found JAR: ${jarPath}`);

// Start the Java application (try multiple Java paths)
const javaPaths = [
    path.join(__dirname, 'runtime-java', 'temurin-21-jdk-amd64', 'bin', 'java'),
    '/usr/lib/jvm/temurin-21-jdk-amd64/bin/java',
    '/usr/local/bin/java',
    '/usr/bin/java',
    'java'
];

let javaPath = path.join(__dirname, 'runtime-java', 'temurin-21-jdk-amd64', 'bin', 'java'); // Default to copied Java
for (const jPath of javaPaths) {
    try {
        require('fs').accessSync(jPath, require('fs').constants.F_OK);
        javaPath = jPath;
        console.log(`Using Java at: ${javaPath}`);
        break;
    } catch (e) {
        console.log(`Java not found at: ${jPath}`);
    }
}

console.log(`Final Java path: ${javaPath}`);

const javaProcess = spawn(javaPath, [
    '-XX:+UseContainerSupport',
    '-XX:MaxRAMPercentage=75.0',
    `-Dserver.port=${process.env.PORT || 8080}`,
    '-jar',
    jarPath
], {
    stdio: 'inherit'
});

javaProcess.on('close', (code) => {
    console.log(`Java process exited with code ${code}`);
    process.exit(code);
});

javaProcess.on('error', (err) => {
    console.error('Failed to start Java process:', err);
    process.exit(1);
});

// Handle shutdown gracefully
process.on('SIGTERM', () => {
    console.log('Received SIGTERM, shutting down gracefully...');
    javaProcess.kill('SIGTERM');
});

process.on('SIGINT', () => {
    console.log('Received SIGINT, shutting down gracefully...');
    javaProcess.kill('SIGINT');
});