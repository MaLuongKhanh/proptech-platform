@echo off
setlocal enabledelayedexpansion

echo === Checking for changes and updating services ===

REM Setting default registry if not configured
if "%REGISTRY%"=="" (
    set REGISTRY=localhost
    echo Using default registry: %REGISTRY%
)

REM Check for recent changes compared to the latest commit
echo Detecting changed services...

REM List of services
set SERVICES=api-gateway config-server discovery-server listing-service

REM Check each service
for %%s in (%SERVICES%) do (
    echo Checking %%s...
    
    REM Check for changes in source code and configuration
    git diff --quiet HEAD -- ..\%%s\src ..\%%s\pom.xml ..\%%s\Dockerfile
    
    if !errorlevel! neq 0 (
        echo Changes detected in %%s, rebuilding...
        
        REM Build Maven project
        cd ..\%%s
        call mvn clean package -DskipTests
        
        REM Build Docker image
        echo Building Docker image for %%s...
        docker build -t %REGISTRY%/proptech/%%s:latest .
        
        REM Push to registry if needed
        if not "%REGISTRY%"=="localhost" (
            echo Pushing image to registry...
            docker push %REGISTRY%/proptech/%%s:latest
        )
        
        REM Update service in Swarm
        echo Updating service in Swarm...
        docker service update --image %REGISTRY%/proptech/%%s:latest proptech_%%s
        
        cd ..\docker-compose
        echo Service %%s updated successfully.
    ) else (
        echo No changes in %%s, skipping.
    )
)

REM Check for changes in Docker configuration files
git diff --quiet HEAD -- docker-compose.swarm.yml prometheus.yml
if !errorlevel! neq 0 (
    echo Changes detected in Docker configuration, redeploying stack...
    docker stack deploy -c docker-compose.swarm.yml proptech
) else (
    echo No changes in Docker configuration.
)

echo === Update process completed ===