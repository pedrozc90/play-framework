.DEFAULT_GOAL := help

SHELL := bash
.SHELLFLAGS := -eu -o pipefail -c

# -- help ---------------------

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*## .*$$' $(MAKEFILE_LIST) \
		| awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

# -- app ----------------------

.PHONY: run
run: ## sbt run
	./run.sh

.PHONY: clean
clean: ## sbt clean compile
	./sbt clean compile
