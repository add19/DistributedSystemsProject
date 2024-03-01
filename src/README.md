# Project owner
Aadish Deshpande (deshpande.aa@northeastern.edu)

# Project Readme

[//]: # (## General guidelines)

[//]: # (* Please spend some time to make a proper `ReadME` markdown file, explaining all the steps necessary to execute your source code.)

[//]: # (* Do not hardcode IP address or port numbers, try to collect these configurable information from config file/env variables/cmd input args.)

[//]: # (* Attach screenshots of your testing done on your local environment.)

## Brief overview of the project
* This project comes with 2 separate clients and 2 separate servers, for TCP and
UDP each
* There are 2 separate packages, for `client` and `server`

### Sample configuration

#### Project structure
* The following is our project structure.
```bash
├── README.md
├── client
│   ├── AbstractClient.java
│   ├── ClientAppTCP.java
│   ├── ClientAppUDP.java
│   ├── ClientLogger.java
│   ├── IClient.java
│   ├── TCPClient.java
│   └── UDPClient.java
├── client_log.txt
├── distributed_systems.iml
├── server
│   ├── AbstractServer.java
│   ├── IServer.java
│   ├── KeyValueStore.java
│   ├── ServerAppTCP.java
│   ├── ServerAppUDP.java
│   ├── ServerLogger.java
│   ├── TCPServer.java
│   └── UDPServer.java
└── server_log.txt
```
* Compile the code using `javac server/*.java client/*.java`
* To run the 
  * TCP server `java server/ServerAppTCP <tcp-port-number>`
  * UDP server `java server/ServerAppUDP <udp-port-number>`
* To run the 
  * TCP client `java client/ClientAppTCP <host-name> <port-number>`
  * UDP client `java client/ClientAppUDP <host-name> <port-number>`
* TCP client communicates with TCP server and UDP client communicates with UDP server
* All the client and server logs are generated automatically even if they don't exist in the project
  * Client logs are generated as `client_log.txt`
  * Server logs are generated as`server_log.txt`


## Executive Summary
For the Project1, requirement of the assignment was to make students understand how TCP and UDP works and what is the 
difference in the implementation of these protocols. Also, through this assignment, we were supposed to learn how 
server and client communicate regardless of the protocol. Through refactoring and modularizing the code, we understood 
how to write clean and easily understandable code. Since, this project required only single threaded server, it was 
intended for us to focus on implementation of 1 request at a time and handle various scenarios such as malformed 
request and implementing timeout at client side when server doesn’t respond in given time. Creating client and server
logs made sure that all the request and response messages are saved in human-readable form to read the status of the 
protocol messages.

While carrying out this project, we faced multiple challenges, for example, while implementing timeout, earlier we
tried implementing a thread for each request, but later we realized that java provides “setSoConnect” functionality 
to assign timeout to the socket. Other than that, we learned a lot about java.net and java.io packages and exceptions 
that can be raised in certain scenarios. Also, handling packets in TCP and UDP was very different, and we had to handle
malformed/out of order packets in UDP using checksums as request IDs which was not required in TCP, so handling the 
requests in different protocols was a challenge for us. Regarding logging, initially we thought of using 3rd party 
logging API, but we implemented our own logging logic and understood the basics of logging and creating timestamps for
each log and message in a text file. We maintained a list of negative edge cases, like server not connected, malformed
package etc. and made sure our client and server are robust to all major failures. Finally, we also made sure while
implementing, we modularized and documented the code to make it readable and extendable.

One use case of such a single threaded server client application could be a “web server” which can be used to handle
HTTP requests. One application where such a web server can be used is a “financial trading systems” where there is 
one server to handle the trade and market data requests of clients to ensure timely and accurate transactions.



# OUTPUT and SCREENSHOTS

* for outputs to different edge cases , please navigate to screenshots folder.