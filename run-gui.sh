#! /bin/bash

# 1. gradle build
./gradlew build

# 2. Set up environment variables
mkdir ~/cool
export COOL_HOME=~/cool
export EXPID=cool

# 3. Run the jar file
cd build/libs
export XQUARTZ_DARK_MODE=0
export GTK_THEME=Adwaita:light
java -cp ./dpux-1.0-SNAPSHOT.jar org.jlab.epsci.dpux.desktop.CDesktopNew
