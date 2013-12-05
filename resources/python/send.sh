#!/bin/zsh
curl -F "filedata=@$1;filename=img" localhost:9000
# curl -F "filedata=@$1;filename=img" http://httpbin.org/post