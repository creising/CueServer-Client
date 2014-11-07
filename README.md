### Build Status
[![Build Status](https://travis-ci.org/creising/CueServer-Client.png)](https://travis-ci.org/creising/CueServer-Client)
[![Coverage Status](https://coveralls.io/repos/creising/CueServer-Client/badge.png?branch=master)](https://coveralls.io/r/creising/CueServer-Client?branch=master)

### Introduction
The CueSever Java client library is intended to facilitate interaction with
CueServer's HTTP interface. In addition to the client, there is also a simple
command line interface to demonstrate the use of the CueServer client.

Currently, the client library does not throttle requests to the CueServer. As
noted in CueServer's documentation, new requests should only be sent after a
response from the previous request has been received from CueServer.

###Project Structure

`./client` contains all code related to the client library.
`./cli` contains all of the code related to the example command line interface.

###Compilation
The client library uses the [Gradle](http://www.gradle.org) build system. If
you're new to Gradle, here are a couple of common commands to get you started.
All commands should be run from the root directory.

`./gradlew build`: compiles all code and runs the unit tests
`./gradlew clean`: removes the build directories
`./gradlew cli:installApp`: creates a JVM application along with libs and OS
specific scripts. Once this command is run, the application can be found in:
`cli/build/install/`

####Dependencies
The client library has the following dependencies:

* Google's guava v18.0
* Apache's httpclient v4.3.5
* Your favorite SLF4J library for logging

### Additional Reading
For further information regarding the CueServer, please visit:
[Interactive Technologies](http://interactive-online.com)

### Supported Operations
The client library currently supports the following operations:

- Retrieval of system information
- Retrieval of playback information
- Retrieval of level from the DMX output
- Play a cue on a playback
- Clear a playback
- Set a channel
- Set a range of channels
- Record a cue
- Update a cue
- Delete a cue

