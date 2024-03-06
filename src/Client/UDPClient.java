package Client;

import java.net.*;
import java.io.*;


/**
 * This class represents a client which communicates to server using UDP protocol. Given a
 * server host IP address and the port number, this client starts communicating with the server
 * at the specified host and port. This class extends the AbstractClient class that has the protocol
 * agnostic functionalities for clients.
 */
public class UDPClient extends AbstractClient {
  // Time out duration, currently set to 5 seconds
  private static final int TIMEOUT_INTERVAL = 5000;

  private static final int BUFFER_SIZE = 65000;

  @Override
  public void startClient(String serverIp, int portNum) {
    try (DatagramSocket aSocket = new DatagramSocket();
      BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
      InetAddress aHost = InetAddress.getByName(serverIp);
      populateKeyValues(aSocket, aHost, portNum);
      while (true) {
        String request = generateRequestFromUserChoice(userInput);
        if(request.isEmpty()) {
          continue;
        }
        sendRequest(aSocket, request, aHost, portNum);

        System.out.print("Do you want to perform another operation? (yes/no): ");
        String anotherOperation = userInput.readLine().toLowerCase();
        if (!anotherOperation.equalsIgnoreCase("yes")) {
          break;
        }
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("Looks like server timed out: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Invalid port number: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Invalid arguments: " + e.getMessage());
    }
  }

  private void handleLargeResponses(DatagramSocket aSocket, DatagramPacket reply, long requestId)
      throws IOException {
    String resp = handleResponse(aSocket, reply, requestId);
    int idx = 0;
    while(!resp.contains("END =>")) {
      resp = handleResponse(aSocket, reply, requestId);
      idx++;
    }
    System.out.println("Expected : " + resp.split("=>")[1] + " Received : " + idx);
    System.out.println(Integer.parseInt(resp.split("=>")[1]) == idx ?
        "No packet loss during transmission of all key values" : "Some packets may be lost");
  }

  private void sendRequest(DatagramSocket aSocket, String requestString, InetAddress aHost,
      int serverPort) throws IOException {

    // Parse request information from the request string.
    String[] requestToken = requestString.split("::");
    String action = requestToken[1];

    // creating datagram packet
    long requestId = generateChecksum(requestString);
    requestString = requestId + "::" + requestString;

    byte[] m = requestString.getBytes();
    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

    // sending datagram packet
    aSocket.send(request);

    // setting timeout of 5 seconds for udp request and waiting for response from server
    aSocket.setSoTimeout(TIMEOUT_INTERVAL);
    byte[] buffer = new byte[BUFFER_SIZE];
    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

    if(action.equalsIgnoreCase("GET ALL")) {
      handleLargeResponses(aSocket, reply, requestId);
    } else {
      handleResponse(aSocket, reply, requestId);
    }
  }

  private String handleResponse(DatagramSocket aSocket, DatagramPacket reply, long requestId)
    throws IOException {
      // receive response
    aSocket.receive(reply);

    String response = new String(reply.getData(), 0, reply.getLength());
    if(response.contains("END =>")) {
      return response;
    }

    String[] responseToken = response.split(":");
    long responseRequestId = Long.parseLong(responseToken[0]);

    // validating malformed responses from server
    if(responseRequestId != requestId) {
      System.out.println("[" + getTimestamp() + "] => " +
        "Received Malformed response for request: " + requestId +
        " ; Received response for " + responseToken[0]);
      return "END =>" + response;
    } else {
      System.out.println("[" + getTimestamp() + "] => " + " Server Reply: " + new String(reply.getData(), 0, reply.getLength()));
    }
    return response;
  }

  private void populateKeyValues(DatagramSocket aSocket, InetAddress aHost, int serverPort)
    throws IOException {
    final int NUM_KEYS = 45000;
    //Pre-populating key value store
    // Send PUT requests
    for (int i = 1; i <= NUM_KEYS * 2; i++) {
      String putString = generateUUID() + "::PUT::key" + i + "::value" + i;
      sendRequest(aSocket, putString, aHost, serverPort);
    }

    // Send GET requests
    for (int i = 1; i <= NUM_KEYS * 2; i++) {
      String getString = generateUUID() + "::GET::key" + i;
      sendRequest(aSocket, getString, aHost, serverPort);
    }

    //DELETE requests
    for (int i = 5; i <= 10; i++) {
      String getString = generateUUID() + "::DELETE::key" + i;
      sendRequest(aSocket, getString, aHost, serverPort);
    }
  }
}
