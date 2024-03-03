package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This represents a UDP server which receives datagram requests, validates them and sends responses
 * to the client. This class extends the abstract server class which has the common implementation
 * to handle key value requests.
 */
public class UDPServer extends AbstractServer {
  @Override
  public void listen(int portNumber) {

    // Server socket creation on specified port number.
    try (DatagramSocket aSocket = new DatagramSocket(portNumber)) {
      serverLogger.logServerMessage("Server active on port " + portNumber);

      while (true) {
        byte[] buffer = new byte[1000];

        // Receiving datagram request
        DatagramPacket request = new DatagramPacket(buffer,
          buffer.length);
        aSocket.receive(request);

        // Validating requests on server side.
        if (!validRequest(request)) {
          String response = "Couldn't process request.";
          serverLogger.logUDPMalformedRequest(request.getAddress(), request.getLength());
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
            response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          continue;
        }

        // parsing and processing request
        String msg = new String(request.getData(), 0, request.getLength());
        serverLogger.logClientRequest(request.getAddress(), msg);
        String[] parsedTokens = parseRequest(msg);
        String response = "";
        if(parsedTokens.length == 0) {
          // return an error string.
          response = "Invalid request format";
        } else {
          // process request from the key value store
          response = processRequest(parsedTokens);
        }

        // handling get all operations
        if(parsedTokens[2].equalsIgnoreCase("GET ALL")) {
          handleHugeResponses(response, request, aSocket);
        } else {
          // sending response back to client
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
            response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          serverLogger.logServerResponse(reply.getAddress(),response);
        }
        System.out.println("DONE SENDING");
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }

  private static long generateChecksum(String[] requestParts) {
    String result = String.join("::", Arrays.copyOfRange(requestParts, 1, requestParts.length));
    byte [] m = result.getBytes();
    Checksum crc32 = new CRC32();
    crc32.update(m, 0, m.length);
    return crc32.getValue();
  }

  /**
   * This function validates a given datagram packet, if it is as per protocol and is not corrupted.
   * @param request Datagram request.
   * @return boolean indicating request is valid or not.
   */
  private boolean validRequest(DatagramPacket request) {

    String requestData = new String(request.getData(), 0, request.getLength());
    String[] parts = requestData.split("::");

    if (parts.length < 3) {
      return false;
    }

    if (parts[0].isEmpty() || parts[1].isEmpty()) {
      return false;
    }

    long responseRequestId = Long.parseLong(parts[0]);

    // compare checksums, if not equal means malformed request.
    return responseRequestId == generateChecksum(parts);
  }

  private void handleHugeResponses(String response, DatagramPacket request, DatagramSocket aSocket)
    throws IOException {
    String[] responseKeyVals = response.split("\n");
    String id = responseKeyVals[0].split(":")[0];

    DatagramPacket reply = new DatagramPacket(responseKeyVals[0].getBytes(),
      responseKeyVals[0].getBytes().length, request.getAddress(), request.getPort());
    aSocket.send(reply);
    serverLogger.logServerResponse(reply.getAddress(),responseKeyVals[0]);

    for(int i=1; i<responseKeyVals.length; i++) {
      String resp = id + ":" + responseKeyVals[i];
      reply = new DatagramPacket(resp.getBytes(),
        resp.getBytes().length, request.getAddress(), request.getPort());
      aSocket.send(reply);
      serverLogger.logServerResponse(reply.getAddress(),resp);
    }
    String resp = "END";
    reply = new DatagramPacket(resp.getBytes(),
      resp.getBytes().length, request.getAddress(), request.getPort());
    aSocket.send(reply);
  }

  private void handleInvalidRequest(DatagramSocket aSocket, DatagramPacket request)
    throws IOException {
    String response = "Couldn't process request.";
    serverLogger.logUDPMalformedRequest(request.getAddress(), request.getLength());
    DatagramPacket reply = new DatagramPacket(response.getBytes(),
      response.getBytes().length, request.getAddress(), request.getPort());
    aSocket.send(reply);
  }
}