#!/bin/zsh
# $1 = input
# $2 = output
# $3 = x
# $4 = y
# $5 = w
# $6 = h
convert $1 +repage $1
convert $1 -crop $5x$6+$3+$4 +repage $2