#!/bin/bash

# Ensure that target dir exists
mkdir ../target

# Download POST response to ${PROJECT_ROOT}/target/addresslabels.csv
curl -i -H "Content-Type: application/json" -X POST -d @../src/test/resources/addresslabel_csv.json http://localhost:8080/api/v1/addresslabel > ../target/addresslabels.csv

