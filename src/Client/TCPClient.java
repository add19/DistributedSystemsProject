package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This is a TCP Client class, that interacts with the server.
 */
public class TCPClient extends AbstractClient {
    public void startClient(String serverIP, int serverPort) {
        Socket socket = null;
        try {
            socket = new Socket(serverIP, serverPort);
            socket.setSoTimeout(5000);
            System.out.println("Connected to the server");
            ClientLogger.log("Connected to the server");
        } catch (IOException e) {
            System.out.println("Couldn't connect to server at mentioned IP and port");
            ClientLogger.log("Couldn't connect to server at mentioned IP and port\"");
            System.exit(1);
        }

        try (
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            populateKeyValues(in, out);

            while (true) {
                String request = generateRequestFromUserChoice(userInput);
                if(request.isEmpty()) {
                    continue;
                }
                sendRequest(out, in, request);

                System.out.print("Do you want to perform another operation? (yes/no): ");
                String anotherOperation = userInput.readLine().toLowerCase();
                if (!anotherOperation.equals("yes")) {
                    break;
                }
            }
        } catch (SocketException ex) {
            System.out.println("Connection terminated by the server...");
            ClientLogger.log("Connection terminated by the server...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long generateChecksum(String requestString) {
        byte [] m = requestString.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(m, 0, m.length);
        return crc32.getValue();
    }

    private boolean isGetAllRequest(String request) {
        return request.split("::")[2].equalsIgnoreCase("GET ALL");
    }

    private void handleHugeResponse(BufferedReader in) throws IOException {
        StringBuilder responseData = new StringBuilder();
        String response;
        System.out.println(in.readLine());
        while (!(response = in.readLine()).equals("END")) {
            responseData.append(response).append("\n");
        }
        System.out.println(responseData);
    }

    private void sendRequest(PrintWriter out, BufferedReader in, String request)
      throws IOException {
        try {
            request = generateChecksum(request) + "::" + request;
            // Send request to server
            out.println(request);
            if(isGetAllRequest(request)) {
                handleHugeResponse(in);
            } else {
                // Receive response from server
                String responseFromServer = in.readLine();
                System.out.println(responseFromServer);
                // Log response
                ClientLogger.log("Response from server: " + responseFromServer);
            }
        } catch (SocketTimeoutException e) {
            String[] strArr = request.split("::");
            String requestId = strArr[0];
            System.out.println("Received no response from the server for request id : "+requestId);
            ClientLogger.log("Received no response from the server for the request id : "+requestId);
        }
    }

    private void populateKeyValues(BufferedReader in, PrintWriter out) {
        final int NUM_KEYS = 45000;
        try {
            // PUT requests
            for (int i = 1; i <= NUM_KEYS*2; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String value = Integer.toString(i * 10);
                String putString = requestId + "::PUT::key" + key + "::value" + value;

                sendRequest(out, in, putString);
                System.out.println("Pre-populated key" + key + " with value " + value);
                ClientLogger.log("Pre-populated key" + key + " with value " + value);
            }
            //GET requests
            for (int i = 1; i <= NUM_KEYS*2; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String getString = requestId + "::GET::key" + key;

                sendRequest(out, in, getString);
                System.out.println("GET key" + key);
                ClientLogger.log("GET key" + key);
            }
            //DELETE requests
            for (int i = 5; i <= 10; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String deleteString = requestId + "::DELETE::key" + key;

                sendRequest(out, in, deleteString);
                System.out.println("DELETE key" + key);
                ClientLogger.log("DELETE key" + key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
