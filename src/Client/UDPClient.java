package Client;

import java.net.*;
import java.io.*;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This represents the UDP client which communicates to the UDP server over a given port and host
 * address.
 */
public class UDPClient extends AbstractClient {
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
      System.out.println("IO: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Invalid port number: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Invalid arguments: " + e.getMessage());
    }
  }

  private String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  private long generateChecksum(String requestString) {
    byte [] m = requestString.getBytes();
    Checksum crc32 = new CRC32();
    crc32.update(m, 0, m.length);
    return crc32.getValue();
  }

  private void handleLargeResponses(DatagramSocket aSocket, DatagramPacket reply, long requestId) {
    String resp = handleResponse(aSocket, reply, requestId);
    int numKvs = Integer.parseInt(resp.split(":")[1]);
    while(numKvs > 1) {
      String response = handleResponse(aSocket, reply, requestId);
      if(response.equals("TRANSFER COMPLETE!")) {
        break;
      }
      numKvs--;
    }
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
    aSocket.setSoTimeout(5000);
    byte[] buffer = new byte[1000];
    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

    if(action.equalsIgnoreCase("GET ALL")) {
      handleLargeResponses(aSocket, reply, requestId);
    } else {
      handleResponse(aSocket, reply, requestId);
    }
    System.out.println("DONE!");
  }

  private String handleResponse(DatagramSocket aSocket, DatagramPacket reply, long requestId) {
    try {
      // receive response
      aSocket.receive(reply);

      String response = new String(reply.getData(), 0, reply.getLength());
      if(response.equals("END")) {
        return "TRANSFER COMPLETE!";
      }

      String[] responseToken = response.split(":");
      long responseRequestId = Long.parseLong(responseToken[0]);

      // validating malformed responses from server
      if(responseRequestId != requestId) {
        ClientLogger.log("Received Malformed response for request: " + requestId +
          " ; Received response for " + responseToken[0]);
      } else {
        ClientLogger.log("Received response " + response);
        System.out.println(" Reply: " + new String(reply.getData(), 0, reply.getLength()));
      }
      return response;
    } catch(SocketTimeoutException e) {
      System.out.println("Request timed out.. received no response from server for request: "
        + requestId);
      ClientLogger.log("Request timed out.. received no response from server for request: "
        + requestId);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "-1";
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
