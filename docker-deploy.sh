#!/bin/bash

# Configuration
IMAGE_NAME="paolobonicco/user-service"
TAG=$(date "+%Y-%m-%d_%H-%M")
DEPLOYMENT_NAME="user-service"
CONTAINER_NAME="user-service"

# Step 1: Build the Docker image
echo "Starting build of the image $IMAGE_NAME:$TAG..."
docker build -t $IMAGE_NAME:$TAG .
if [ $? -ne 0 ]; then
    echo "Error in building the image."
    exit 1
fi

# Step 2: Push the image to Docker Hub
echo "Starting push of the image $IMAGE_NAME:$TAG to Docker Hub..."

# echo "Enter Docker Hub password:"
# docker login --username my-user --password-stdin

docker login
docker push $IMAGE_NAME:$TAG
if [ $? -ne 0 ]; then
    echo "Error in pushing the image."
    exit 1
fi

# Step 3: Update the Kubernetes deployment
echo "Starting update of the Kubernetes deployment..."
kubectl set image deployment/$DEPLOYMENT_NAME $CONTAINER_NAME=$IMAGE_NAME:$TAG --record
if [ $? -ne 0 ]; then
    echo "Error in updating the deployment."
    exit 1
fi

echo "Deployment completed successfully!"