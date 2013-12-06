#!/bin/zsh
# curl -F "filedata=@$1;filename=img" localhost:9000
curl -F "filedata=@$1;filename=img" http://172.16.148.159:9000
# curl -F "filedata=@$1;filename=img" http://httpbin.org/post