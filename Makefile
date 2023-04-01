SHELL := /bin/bash

.DEFAULT_GOAL := help
.PHONY: help
help:
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

up: ## Run a local development environment with Docker Compose.
	@docker compose up -d --build --force-recreate 

down: ## Stop the Docker Compose local development environment.
	@docker compose down  

recreate: ## Recreate and run development docker compose
	@docker compose up --build --force-recreate 

clean: ## Clean Docker Compose local development environment.
	@docker compose down --remove-orphans --volumes

tables: ## Create Tables based on migration.sql
	@docker exec -i postgres_mtcg psql -U postgres -d mtcg < migration.sql

