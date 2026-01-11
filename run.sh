#!/usr/bin/env bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Default configuration
CLEAN=false
AUTO_APPLY_EVOLUTIONS=true
SBT_COMMAND="./sbt"

# Function to print usage
print_usage() {
    echo -e "${YELLOW}Usage: $0 [OPTIONS]${NC}"
    echo "Options:"
    echo "  -c, --clean                Clean before running"
    echo "  -h, --help                 Show this help message"
    echo "  --no-evolutions            Disable auto-applying evolutions"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -c|--clean)
            CLEAN=true
            shift
            ;;
        --no-evolutions)
            AUTO_APPLY_EVOLUTIONS=false
            shift
            ;;
        -h|--help)
            print_usage
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            print_usage
            exit 1
            ;;
    esac
done

# Function to check if local sbt exists
check_sbt() {
    if [ ! -f "$SBT_COMMAND" ]; then
        echo -e "${RED}Error: sbt file not found in project root${NC}"
        exit 1
    fi
    chmod +x "$SBT_COMMAND"
}

# Function to run the cleanup
cleanp() {
    echo -e "${YELLOW}Cleaning project...${NC}"
    $SBT_COMMAND clean
    if [ $? -ne 0 ]; then
        echo -e "${RED}Clean failed${NC}"
        exit 1
    fi
}

# Function to compile the project
compile() {
    echo -e "${YELLOW}Compiling project...${NC}"
    $SBT_COMMAND compile
    if [ $? -ne 0 ]; then
        echo -e "${RED}Compilation failed${NC}"
        exit 1
    fi
}

# Main execution
echo -e "${GREEN}Starting Play Framework application...${NC}"

# Check if sbt exists and is executable
check_sbt

# Clean if requested
if [ "$CLEAN" = true ]; then
    cleanp
fi

# Always compile
compile

# Prepare Java options
if [ "$AUTO_APPLY_EVOLUTIONS" = true ]; then
    export JAVA_OPTS="-Dplay.evolutions.db.default.autoApply=true"
fi

# Run the application
echo -e "${GREEN}Running application...${NC}"
echo -e "${YELLOW}Java opts: $JAVA_OPTS${NC}"

$SBT_COMMAND -jvm-debug 9999 run
