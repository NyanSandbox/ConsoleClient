#!/bin/bash
mvn clean package && cd target/ && java -jar console-client-0.0.1-SNAPSHOT.jar && cd ../
