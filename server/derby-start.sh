#!/bin/bash

# Check if the directory ./db exists
if [ -d "./db" ]; then
  # If it exists, delete the directory
  rm -rf ./db
fi

# Continue with the rest of your script
cd db
./db-derby-10.15.2.0-bin/bin/startNetworkServer -noSecurityManager
