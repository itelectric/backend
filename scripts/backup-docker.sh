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
BACKUP_DIR="$HOME/iteletric-dbdumps"
BACKUP_FILE="${DATABASE_NAME}-backup.dump"

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

# Execute pg_dump inside the container
sudo docker exec -t "$CONTAINER_NAME" pg_dump -U postgres -d "$DATABASE_NAME" -F c -f "/$BACKUP_FILE"

# Copy the backup to the host machine
sudo docker cp "$CONTAINER_NAME":/"$BACKUP_FILE" "$BACKUP_DIR/"

# Remove the file inside the container
sudo docker exec -t "$CONTAINER_NAME" rm "/$BACKUP_FILE"

echo "Backup completed: $BACKUP_DIR/$BACKUP_FILE"
