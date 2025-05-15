# PropTech Platform

A modern real estate platform built with microservices architecture.

## Project Overview

PropTech Platform is a comprehensive real estate solution that connects property owners, agents, and buyers. The platform offers features like property listings, user management, payment processing, and more.

## Technology Stack

### Backend
- **API Gateway**: Spring Cloud Gateway
- **Config Server**: Centralized configuration management
- **Discovery Server**: Service discovery with Eureka
- **Listing Service**: Property listing management
- **Payment Service**: Payment processing and wallet management
- **User Service**: User authentication and management
- **Sale Service**: Property transaction management
- **Rental Service**: Property rental management
- **Database**: MongoDB
- **Message Broker**: RabbitMQ
- **Monitoring**: 
  - Prometheus for metrics collection
  - Grafana for visualization and monitoring
- **Container Orchestration**: Docker Swarm
- **Service Mesh**: Spring Cloud
- **Security**: Spring Security, JWT
- **API Documentation**: Swagger/OpenAPI

### Frontend
- **Framework**: React with TypeScript
- **UI Library**: Material-UI (MUI)
- **State Management**: React Context
- **Routing**: React Router
- **HTTP Client**: Axios
- **Animation**: Framer Motion
- **Image Upload**: Cloudinary

## Project Structure

### Frontend Structure
```
frontend/
├── assets/         # Static assets (images, icons)
├── components/     # Reusable UI components
├── constants/      # Application constants
├── context/        # React context providers
├── hooks/          # Custom React hooks
├── pages/          # Page components
├── services/       # API service calls
├── store/          # State management
├── styles/         # Global styles and CSS modules
├── types/          # TypeScript type definitions
├── utils/          # Utility functions
├── app.tsx         # Main application component
├── index.tsx       # Application entry point
└── .env            # Environment variables
```

### Backend Structure
```
backend/
├── api-gateway/           # API Gateway service
├── config-server/         # Configuration service
├── discovery-server/      # Service discovery
├── listing-service/       # Property listing service
├── payment-service/       # Payment processing service
├── user-service/         # User management service
├── sale-service/         # Transaction service
└── docker-compose/       # Docker configuration
    ├── docker-compose.swarm.yml
    ├── deploy-swarm.bat
    ├── deploy-swarm.sh
    └── .env              # Environment variables
```

## Prerequisites

- JDK 17+
- Maven
- Node.js 16+
- Docker and Docker Compose
- Docker Swarm (for production deployment)

## Environment Variables

### Frontend (.env)
```
PORT=5173
REACT_APP_API_URL=http://localhost:8080/api
```

### Backend (.env in docker-compose)
```
CLOUDINARY_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

## Getting Started

### Development Setup

1. Clone the repository
```bash
git clone https://github.com/your-username/proptech-platform.git
cd proptech-platform
```

2. Start the backend services
```bash
cd backend/docker-compose
docker-compose up -d
```

3. Start the frontend development server
```bash
cd frontend
npm install
npm run dev
```

### Production Deployment

The platform can be deployed using Docker Swarm for better scalability and reliability.

#### Using Windows (deploy-swarm.bat)
```bash
cd backend/docker-compose
deploy-swarm.bat
```

#### Using Linux/Mac (deploy-swarm.sh)
```bash
cd backend/docker-compose
chmod +x deploy-swarm.sh
./deploy-swarm.sh
```

The deployment script will:
1. Check and initialize Docker Swarm if needed
2. Build Docker images
3. Push images to registry (if using external registry)
4. Deploy the stack to Docker Swarm

## Features

- User authentication and authorization
- Property listing management
- Real-time property search
- Payment processing
- Wallet management
- User role management (Admin, Agent, User)
- Service package management
- Transaction history
- Responsive design

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.