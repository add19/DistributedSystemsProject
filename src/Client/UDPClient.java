package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
        if (!anotherOperation.equals("yes")) {
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

  private static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  private static long generateChecksum(String requestString) {
    byte [] m = requestString.getBytes();
    Checksum crc32 = new CRC32();
    crc32.update(m, 0, m.length);
    return crc32.getValue();
  }

  private static void sendRequest(DatagramSocket aSocket, String requestString, InetAddress aHost,
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

    try {
      // receive response
      aSocket.receive(reply);
      String response = new String(reply.getData(), 0, reply.getLength());
      String[] responseToken = response.split(":");
      long responseRequestId = Long.parseLong(responseToken[0]);

      // validating malformed responses from server
      if(responseRequestId != requestId) {
        ClientLogger.log("Received Malformed response for request: " + requestId +
          " ; Received response for " + responseToken[0]);
      } else {
        ClientLogger.log("Received response " + response);
        System.out.println(action+" Reply: " + new String(reply.getData(), 0, reply.getLength()));
      }
    } catch(SocketTimeoutException e) {
      System.out.println("Request timed out.. received no response from server for request: "
        + requestId);
      ClientLogger.log("Request timed out.. received no response from server for request: "
          + requestId);
    }
  }

  private static void populateKeyValues(DatagramSocket aSocket, InetAddress aHost, int serverPort)
    throws IOException {
    final int NUM_KEYS = 10;
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
    for (int i = 5; i <= NUM_KEYS*2; i++) {
      String getString = generateUUID() + "::DELETE::key" + i;
      sendRequest(aSocket, getString, aHost, serverPort);
    }
  }
}