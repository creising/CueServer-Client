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

### Details
For further information regarding the cue server, please visit:
[Interactive Technologies](http://interactive-online.com)

### Supported Operations
Version 1.0 of the client library currently supports the following operations:

- Retrieval of system information
- Retrieval of playback information
- Retrieval of level from the DMX output

