#!/bin/bash

# Check if the arguments were passed correctly
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <container_name> <database_name>"
    sleep 10
    exit 1
fi

# Variables
CONTAINER_NAME=$1
DATABASE_NAME=$2
USER_NAME=$(basename $HOME)

# Detect the OS and set the backup directory
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
    # Windows
    BACKUP_DIR="/c/Users/$USER_NAME/iteletric-dbdumps"
    USE_SUDO="false"
else
    # Linux/Mac
    BACKUP_DIR="$HOME/iteletric-dbdumps"
    USE_SUDO="true"
fi

# Get the latest backup file
BACKUP_FILE="$(ls -t "$BACKUP_DIR" | grep "${DATABASE_NAME}-backup" | head -n 1)"

# Check if the container exists
if [ "$USE_SUDO" == "true" ]; then
    COMMAND="sudo docker ps -a --format '{{.Names}}' | grep -q \"^$CONTAINER_NAME$\""
else
    COMMAND="docker ps -a --format '{{.Names}}' | grep -q \"^$CONTAINER_NAME$\""
fi

if ! eval "$COMMAND"; then
    echo "Error: Container '$CONTAINER_NAME' does not exist."
    sleep 10
    exit 1
fi

# Check if the container is running
if [ "$USE_SUDO" == "true" ]; then
    COMMAND="sudo docker ps --format '{{.Names}}' | grep -q \"^$CONTAINER_NAME$\""
else
    COMMAND="docker ps --format '{{.Names}}' | grep -q \"^$CONTAINER_NAME$\""
fi

if ! eval "$COMMAND"; then
    echo "Error: Container '$CONTAINER_NAME' is not running."
    sleep 10
    exit 1
fi

# Check if a backup file exists
if [ -z "$BACKUP_FILE" ]; then
    echo "No backup file found for database: $DATABASE_NAME"
    sleep 10
    exit 1
fi

docker cp "$BACKUP_DIR/$BACKUP_FILE" $CONTAINER_NAME:/tmp/$BACKUP_FILE

# Restore the database
if [ "$USE_SUDO" == "true" ]; then
    # For Linux/Mac, use the backup directory directly
    sudo docker exec -t "$CONTAINER_NAME" bash -c "
      psql -U postgres -c \"DROP DATABASE IF EXISTS $DATABASE_NAME;\";
      psql -U postgres -c \"CREATE DATABASE $DATABASE_NAME;\";
      pg_restore -U postgres -d $DATABASE_NAME -F c \"/tmp/$BACKUP_FILE\"
    "
#    sudo docker exec -t "$CONTAINER_NAME" pg_restore -U postgres -d "$DATABASE_NAME"  -F c "/tmp/$BACKUP_FILE"
else
   # For Windows, the path will have to be mapped to the correct path inside the Docker container
#    docker exec -it "$CONTAINER_NAME" pg_restore -U postgres -d "$DATABASE_NAME"  -F c "/tmp/$BACKUP_FILE"
    winpty docker run --rm -v "$BACKUP_DIR":/backup -it postgres pg_restore -U postgres -d "$DATABASE_NAME" -F c "/backup/$BACKUP_FILE"
fi

echo "Restore completed from: $BACKUP_DIR/$BACKUP_FILE"
sleep 10
