@echo off
echo Checking Docker Swarm...

docker info | findstr "Swarm: active" >nul
if %errorlevel% neq 0 (
    echo Docker Swarm is not initialized.
    echo Initializing Docker Swarm...
    docker swarm init
) else (
    echo Docker Swarm is already initialized.
)

rem Setting up registry if needed
if "%REGISTRY%"=="" (
    set REGISTRY=localhost
    echo Using default registry: %REGISTRY%
) else (
    echo Using registry: %REGISTRY%
)

rem Build the images
echo Building Docker images...
docker-compose -f docker-compose.swarm.yml build

rem Push images if using external registry
if not "%REGISTRY%"=="localhost" (
    echo Pushing images to registry %REGISTRY%...
    docker-compose -f docker-compose.swarm.yml push
)

rem Deploy stack to Swarm
echo Deploying PropTech platform to Docker Swarm...
docker stack deploy -c docker-compose.swarm.yml proptech

echo Deployment successful! Check status with command:
echo docker stack services proptech