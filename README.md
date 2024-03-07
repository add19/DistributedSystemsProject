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

### Assignment Overview:
The purpose of this assignment was to get a deeper understanding of underlying network protocols and 
explore and learn to use programming language based abstractions for implementing these
protocols. Implementing a single threaded server helps to understand the technical nuances 
associated with UDP packets handling, packet losses and malformed request. The assignment helped
focus on the technical differences in the implementations of TCP and UDP protocols, error handling 
logging mechanisms, which help in troubleshooting any application level issues by writing logs in
human-readable formats. 

### Rationale for supporting protocols and Newer Operations:

TCP protocol being connection oriented makes it easy for application programming, in
the sense, as a programmer I don't have to bother about handling sessions, managing connections, 
account for missing packages, etc. as this is already being taken care of. Thus, implementation wise,
TCP based client-server interaction is relatively straightforward. Although proper error handling, 
timeout mechanism using socket operations were needed to be implemented for robust communication 
between TCP based client and server.

Handling state-altering operations like DELETE ALL and GET ALL were relatively easier to implement
as there was no need to check any packet losses during transmission or reliability of network 
communication. In addition, TCP does support transmission of arbitrarily large streams of data, hence
it is much more straightforward. For supporting large streams, in case of network transmission failures,
there are sufficient safeguards and error logging.

UDP protocol on the other hand, involves a lot of stuff like handling and reporting missing packets, 
out of order delivery and malformed packets. There is a mechanism for determining the packet ordering, 
by using checksums. Packet data is used, along with a random uuid for generating unique checksums for 
any types of requests to generate the request id. This request ID is then used to verify the packet ordering
and sequencing.

For GET ALL, especially when transmitting large set of data, I initially considered splitting the 
data into chunks and sending them as separate datagram packets holding a few number of key value 
pairs, However, I wanted to avoid the associated pitfalls of splitting incorrectly some packets at 
the partition of packets. I considered sending each key value pair as a response to the client. 
This ensures upto 70000 key value entries being transmitted without any packet losses and minimal integrity issues. 
The amount of packets that are supposed to arrive from the server and the actual number of packets(key value pairs) 
are logged on the client side on console. Deletion operation results in the clearing of data store, 
operation agnostic.

Identifying any packet losses first and then logging them was a challenge. In addition, buffers 
had to be carefully utilized in order to avoid any potential issues with improper usage.

### Practical Applications:
One use case of such a single threaded server client application could be a “web server” which can be used to handle
HTTP requests. One application where such a web server can be used is a “financial trading systems” where there is
one server to handle the trade and market data requests of clients to ensure timely and accurate transactions.

### Technical impressions.


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





# OUTPUT and SCREENSHOTS

* for outputs to different edge cases , please navigate to screenshots folder.