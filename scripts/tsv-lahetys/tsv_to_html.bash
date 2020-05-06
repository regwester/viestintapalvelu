#!/usr/bin/env bash 

#Converts a delimited file to a HTML table
#Jadu Saikia http://unstableme.blogspot.in

NOARG=64

DEFAULTDELIMITER="\t"
#If no delimiter is supplied, default delimiter is tab
SEPARATOR=${DELIMITER:-$DEFAULTDELIMITER}

printf "<table border=\"1\">"
sed "s/$SEPARATOR/<\/td><td>/g" /dev/stdin | while read line
  do
    printf "<tr><td>${line}</td></tr>\n"
  done
printf "</table>\n"
