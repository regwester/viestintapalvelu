#!/usr/bin/env bash

set -o pipefail

if [ -z $3 ]; then
  echo "Usage: $0 <csv file> <email subject> <ryhmasahkoposti JSESSIONID> [environment virkailija URL]"
  exit 2
fi

IFS=$'\n'
for EMAIL in $(./distinct_in_column.py 0 < "$1"); do
    ./column_matches.py 0 "$EMAIL" < "$1" | ./remove_column.py 0 | ./csv_to_html.py | ./laheta.bash "$EMAIL" "$2" "$3" "$4"
done
