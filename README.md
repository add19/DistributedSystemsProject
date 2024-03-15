# Project owner
Aadish Deshpande (deshpande.aa@northeastern.edu)

# Project Readme

## Brief overview of the project
* This project is an extension of the previous version of this project of TCP and UDP client-server 
key value store application. RMI(Remote Method Invocation) is leveraged for making remote procedural 
calls for client server communication.
* There are 2 packages each for client and server, namely `RMIClient` and `RMIServer`

### Sample configuration

#### Project structure
* Following are the new additions to the existing project structure.
```bash
.
├── ExecutiveSummary.txt
├── RMIClient
│   ├── ClientWorker.java
│   └── KeyValueClient.java
└── RMIServer
    ├── IRemoteDataStore.java
    ├── KeyValueServer.java
    └── RemoteDataStore.java

```
* Compile the code using `javac RMIServer/*.java RMIClient/*.java`
* To run the server
  * `java RMIServer.KeyValueServer <port-number>`
* To run the client
  * `java RMIClient.KeyValueClient <host-ip> <port-number>`
* All the client and server logs are generated on the console

## Executive Summary

### Assignment Overview:(Purpose and Scope)

The purpose of this assignment was to go on to the next level of understanding client-server network communication, using Remote Procedural Calls. This is a level above the traditional
client-server communication using lower-level network protocols like TCP or UDP. Making use of the
RPC paradigms simplify a lot of client-server communication constructs like socket creation, socket
management, and packet level management of data transmission. The simplicity of RPC constructs helped reduce
the complexity of the task of making client-server communication robust in addition to making the server
capable of handling multiple client requests. 
Using RMI facilitates remote communication by allowing us to create a publicly accessible remote server
object. Using RMI helps minimize the difference between working with local
and remote objects. 

### Technical impressions:
Implementing RPC communication using Java RMI provided a straightforward way to achieve the 
assignment's objectives. When a client makes a request to a remote object, RMI creates a new thread 
to handle the request, facilitating multiple client requests to be handled by the server.
The server side possesses the remote object whose methods can be called from different JVM. The stub acts as a gateway for the client side. where all the outgoing requests
can be routed through it. It initiates communication with the remote object, marshals and writes parameters to the remote JVM.
The skeleton on the other hand, reads parameters for remote method call,
invoke the target methods, and marshal and return the response to the callers. 
On server, creation of remotely accessible object and registering  them with the RMI registry was implemented. 
On the client-side, performing lookup of these remote objects for communication was implemented. 
To handle race conditions, ConcurrentHashMap is used. Internally, it uses segmenting. It divides the map into segments. 
Each segment is protected by a lock. This enables multiple threads to access the map concurrently, without race.
When a thread wants to modify a value in the map, it first acquires the lock for the segment that contains the value. 
Once the lock is acquired, the thread can safely modify the value. When the thread is finished, it releases the lock.
Multiple calls for the same key are synchronized.
Using synchronized methods prevents concurrent access to a block of code or object by multiple threads, preventing potential race conditions.
This assignment helped learn more about RPC communication and concurrent programming using object oriented constructs 
with RMI and use of concurrent collections.

# Output and Screenshots

## Client
Client processing and user interaction
![](/Users/aadishdeshpande/Documents/spring/24/distributed_systems/DistributedSystemsProject/Screenshots/p2/Screenshot 2024-03-15 at 4.08.58 PM.png)

Get responses
![](/Users/aadishdeshpande/Documents/spring/24/distributed_systems/DistributedSystemsProject/Screenshots/p2/Screenshot 2024-03-15 at 4.11.32 PM.png)

Put
![](/Users/aadishdeshpande/Documents/spring/24/distributed_systems/DistributedSystemsProject/Screenshots/p2/Screenshot 2024-03-15 at 4.13.35 PM.png)

Delete
![](/Users/aadishdeshpande/Documents/spring/24/distributed_systems/DistributedSystemsProject/Screenshots/p2/Screenshot 2024-03-15 at 4.15.43 PM.png)

