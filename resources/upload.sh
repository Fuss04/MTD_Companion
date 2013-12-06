#!/bin/bash

curl --digest -u d5rofl7bu6c3vgyctcpi:HURqVRoDyh0yZ96p "http://api.moodstocks.com/v2/ref/$1" --form image_file=@"$2" -X PUT
curl --digest -u d5rofl7bu6c3vgyctcpi:HURqVRoDyh0yZ96p "http://api.moodstocks.com/v2/ref/$1/offline" -X POST