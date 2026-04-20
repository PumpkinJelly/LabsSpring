#!/bin/bash

# Docker Compose helper script for Lab4 application

case "$1" in
    "start")
        echo "🚀 Starting Lab4 application with Docker Compose..."
        docker-compose up --build -d
        echo "✅ Application started!"
        echo "   App: http://localhost:8080"
        echo "   PostgreSQL: localhost:5432"
        echo "   MongoDB: localhost:27017"
        echo ""
        echo "📊 Check health status:"
        echo "   curl http://localhost:8080/actuator/health"
        ;;
    "stop")
        echo "🛑 Stopping Lab4 application..."
        docker-compose down
        echo "✅ Application stopped!"
        ;;
    "logs")
        echo "📜 Showing logs..."
        docker-compose logs -f ${2:-app}
        ;;
    "health")
        echo "🏥 Checking health status..."
        curl -s http://localhost:8080/actuator/health | jq .
        ;;
    "clean")
        echo "🧹 Cleaning up containers and volumes..."
        docker-compose down -v
        echo "✅ Cleaned up!"
        ;;
    "ps")
        echo "📋 Running containers:"
        docker-compose ps
        ;;
    *)
        echo "Lab4 Docker Helper"
        echo ""
        echo "Usage: $0 {command}"
        echo ""
        echo "Commands:"
        echo "  start    - Start application with docker-compose"
        echo "  stop     - Stop application"
        echo "  logs     - Show logs (optional: app|postgres|mongodb)"
        echo "  health   - Check application health"
        echo "  ps       - Show running containers"
        echo "  clean    - Clean up containers and volumes"
        echo ""
        echo "Examples:"
        echo "  $0 start"
        echo "  $0 logs app"
        echo "  $0 health"
        ;;
esac
