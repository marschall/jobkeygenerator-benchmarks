#!/bin/sh


export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
java -jar target/jobkeygenerator-benchmarks.jar 8

export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-11.jdk/Contents/Home
java -jar target/jobkeygenerator-benchmarks.jar 11

export JAVA_HOME=/Users/marschall/tmp/zulu17.0.81-ea-jdk17.0.0-ea.35-macosx_x64/zulu-17.jdk/Contents/Home
java -jar target/jobkeygenerator-benchmarks.jar 17

