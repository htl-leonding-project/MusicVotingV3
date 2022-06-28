#!/bin/bash

export IP_ADDRESS=$(ip -4 addr show wlp0s20f3 | grep -oP '(?<=inet\s)\d+(\.\d+){3}')

echo $IP_ADDRESS

export QUARKUS_HTTP_HOST=$IP_ADDRESS

gnome-terminal -- sh -c "./database.sh"
gnome-terminal -- sh -c "./quarkus.sh"
gnome-terminal -- sh -c "./addmusic.sh"
gnome-terminal -- sh -c "./showmusic.sh"
