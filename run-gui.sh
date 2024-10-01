#! /bin/bash

# 1. gradle build
./gradlew build

# 2. Set up environment variables
mkdir ~/cool
export COOL_HOME=~/cool
export EXPID=cool

# 3. Run the jar file
cd build/libs
java -cp ./dpux-1.0-SNAPSHOT.jar org.jlab.epsci.dpux.desktop.CDesktopNew
