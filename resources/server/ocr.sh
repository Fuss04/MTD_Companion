#!/bin/zsh
convert $1.png $1.tif > /dev/null
tesseract $1.tif $1 > /dev/null
rm $1.tif