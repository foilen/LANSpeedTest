#!/bin/bash

set -e

RUN_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $RUN_PATH

echo ----[ Bundle apps ]----

mkdir -p apps
cp LANSpeedTest-desktop/build/libs/LANSpeedTest-desktop-*-all.jar apps

echo ----[ Applications ]----
find apps
