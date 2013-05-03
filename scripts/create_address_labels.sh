#!/bin/bash

# Ensure that target dir exists
mkdir ../target

# Download POST response to ${PROJECT_ROOT}/target/addresslabels.pdf
curl -i -H "Content-Type: application/json" -X POST -d @../src/test/resources/addresslabelbatch1.json http://localhost:8080/api/v1/addresslabel > ../target/addresslabels.pdf

