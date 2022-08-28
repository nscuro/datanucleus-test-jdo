#!/usr/bin/env bash

docker run -d --rm --name mysql \
  -e "MYSQL_RANDOM_ROOT_PASSWORD=yes" \
  -e "MYSQL_DATABASE=test" \
  -e "MYSQL_USER=test" \
  -e "MYSQL_PASSWORD=test" \
  -p "127.0.0.1:3306:3306" \
  mysql:5.7
