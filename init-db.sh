#!/bin/bash
set -e

echo "Creando múltiples bases de datos: $POSTGRES_MULTIPLE_DATABASES"

for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
  echo "Creando base de datos: $db"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<EOSQL
CREATE DATABASE "$db";
GRANT ALL PRIVILEGES ON DATABASE "$db" TO "$POSTGRES_USER";
EOSQL
done
