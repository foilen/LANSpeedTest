LAN Speed Test
========

Easily check the download/upload speed between your computers on your LAN

----

To compile
=

cd LANSpeedTest-core

../gradlew shadow

The jar is in build/libs/LANSpeedTest-core-1.0-all.jar


To execute
=

java -jar build/libs/LANSpeedTest-core-1.0-all.jar --server

java -jar build/libs/LANSpeedTest-core-1.0-all.jar --client
