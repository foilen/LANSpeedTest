#!/bin/bash

set -e

# Get the directory path of the current script
RUN_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $RUN_PATH

rm -rf apps
mkdir apps

./gradlew shadow

cp LANSpeedTest-desktop/build/libs/LANSpeedTest-desktop-*-all.jar apps

echo ---[ Applications compiled ]---
find apps
