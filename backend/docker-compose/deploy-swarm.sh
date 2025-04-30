#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if Docker Swarm is initialized
echo -e "${YELLOW}Checking Docker Swarm...${NC}"
SWARM_STATUS=$(docker info | grep -i "swarm: active" || echo "")

if [ -z "$SWARM_STATUS" ]; then
    echo -e "${RED}Docker Swarm is not initialized.${NC}"
    echo -e "${YELLOW}Initializing Docker Swarm...${NC}"
    docker swarm init
else
    echo -e "${GREEN}Docker Swarm is already initialized.${NC}"
fi

# Setting up registry if needed
if [ -n "$REGISTRY" ]; then
    echo -e "${YELLOW}Using registry: $REGISTRY${NC}"
else
    REGISTRY="localhost"
    echo -e "${YELLOW}Using default registry: $REGISTRY${NC}"
fi

# Build the images
echo -e "${YELLOW}Building Docker images...${NC}"
docker-compose -f docker-compose.swarm.yml build

# Push images if using external registry
if [ "$REGISTRY" != "localhost" ]; then
    echo -e "${YELLOW}Pushing images to registry $REGISTRY...${NC}"
    docker-compose -f docker-compose.swarm.yml push
fi

# Deploy stack to Swarm
echo -e "${YELLOW}Deploying PropTech platform to Docker Swarm...${NC}"
docker stack deploy -c docker-compose.swarm.yml proptech

echo -e "${GREEN}Deployment successful! Check status with command:${NC}"
echo -e "${YELLOW}docker stack services proptech${NC}"