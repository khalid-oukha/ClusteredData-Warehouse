.PHONY: help build test clean run docker-build docker-up docker-down docker-logs docker-clean package

help: ## Show this help message
	@echo "Available commands:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

build: ## Build the application with Maven
	./mvnw clean package -DskipTests

test: ## Run all tests
	./mvnw test

test-coverage: ## Run tests with coverage report
	./mvnw clean verify jacoco:report

clean: ## Clean build artifacts
	./mvnw clean

run: ## Run the application locally
	./mvnw spring-boot:run

package: ## Package as WAR file
	./mvnw clean package

docker-build: ## Build Docker images
	docker-compose build

docker-up: ## Start all services with Docker Compose
	docker-compose up -d

docker-down: ## Stop all services
	docker-compose down

docker-logs: ## View application logs
	docker-compose logs -f app

docker-logs-db: ## View database logs
	docker-compose logs -f postgres

docker-restart: ## Restart application service
	docker-compose restart app

docker-clean: ## Stop services and remove volumes
	docker-compose down -v

docker-rebuild: ## Rebuild and restart services
	docker-compose down
	docker-compose up --build -d

docker-ps: ## Show running containers
	docker-compose ps

db-connect: ## Connect to PostgreSQL database
	docker exec -it warehouse-db psql -U admin -d fx_deals_warehouse

install: ## Install Maven dependencies
	./mvnw dependency:resolve

validate: ## Validate project configuration
	./mvnw validate

format: ## Format code (if formatter plugin configured)
	./mvnw formatter:format

all: clean build docker-rebuild ## Clean, build and deploy everything

dev: docker-up ## Start development environment
	@echo "Development environment started!"
	@echo "Application: http://localhost:8082"
	@echo "Database: localhost:5433"

