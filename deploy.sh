#!/bin/bash

# Matar cualquier proceso Java que ejecute un jar del proyecto
pkill -f ".jar" || true

# Ejecutar el jar generado por Maven
nohup java -jar target/*.jar > log.txt 2>&1 &