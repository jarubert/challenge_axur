#!/bin/sh

until nc -z -v -w30 $RABBITMQ_HOST 5672
do
  echo "Waiting for rabbitmq connection..."
  # wait for 5 seconds before check again
  sleep 2
done


until nc -z -v -w30 'mysql' 3306
do
  echo "Waiting for database connection..."
  # wait for 5 seconds before check again
  sleep 2
done


exec "$@"
