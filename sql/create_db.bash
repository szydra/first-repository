#!/bin/bash
# Script creating the database

if [ $# -lt 1 ]
then
	echo "Jako argument należy podać nazwę pliku z bazą danych."
else
	sqlite3 $1 < $PWD/create_db.sql
	echo "Utworzono bazę danych."
fi
