@echo off
setlocal enabledelayedexpansion

REM Configuration
set "IMAGENAME=user-service"
for /f "tokens=2 delims==" %%i in ('wmic os get localdatetime /value') do set datetime=%%i
set "TAG=%datetime:~0,4%-%datetime:~4,2%-%datetime:~6,2%%datetime:~8,2%-%datetime:~10,2%"
set "DEPLOYMENT_NAME=user-service"
set "CONTAINER_NAME=user-service"

REM Step 1: Build the Docker image
echo Starting build of the image %IMAGE_NAME%:%TAG%...
docker build -t %IMAGE_NAME%:%TAG% .
if %errorlevel% neq 0 (
    echo Error in building the image.
    exit /b 1
)

REM Step 2: Push the image to Docker Hub
echo Starting push of the image %IMAGE_NAME%:%TAG% to Docker Hub...

REM Uncomment and modify the following line if you want to login automatically
REM echo YourDockerHubPassword| docker login --username YourDockerHubUsername --password-stdin

docker login
docker push %IMAGE_NAME%:%TAG%
if %errorlevel% neq 0 (
    echo Error in pushing the image.
    exit /b 1
)

REM Step 3: Update the Kubernetes deployment
echo Starting update of the Kubernetes deployment...
kubectl set image deployment/%DEPLOYMENT_NAME% %CONTAINER_NAME%=%IMAGE_NAME%:%TAG% --record
if %errorlevel% neq 0 (
    echo Error in updating the deployment.
    exit /b 1
)

echo Deployment completed successfully!
endlocal