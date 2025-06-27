#!/bin/bash

export IP_ADDRESS=$(ip -4 addr show wlp0s20f3 | grep -oP '(?<=inet\s)\d+(\.\d+){3}')

echo $IP_ADDRESS

sed -r -i 's/(\b[0-9]{1,3}\.){3}[0-9]{1,3}\b'/"$IP_ADDRESS"/ ../addMusic/src/environments/environment.ts 


sed -r -i 's/(\b[0-9]{1,3}\.){3}[0-9]{1,3}\b'/"$IP_ADDRESS"/ ../showMusic/src/environments/environment.ts 



export QUARKUS_HTTP_HOST=$IP_ADDRESS

./database.sh
./quarkus.sh
./addmusic.sh
./showmusic.sh

