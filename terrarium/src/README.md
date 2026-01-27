# Backend Application

## Requirements
- java 21
- database in correct format

## Instructions

1) Fill application.properties with correct data for the database


2) Create .jar file using Maven

Run:
mvn clean package


3) Put created .jar file on the server

(We used FileZilla for file transfer)


4) Start application on server

Run:
nohup java -jar nazwa_snapshota.jar > app.log 2>&1 &


Application starts on port:
8081

