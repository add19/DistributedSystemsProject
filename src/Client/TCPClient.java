package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * This class represents a client which communicates to server using TCP protocol. Given a
 * server host IP address and the port number, this client starts communicating with the server
 * at the specified host and port. This supports reliable and connection oriented communication
 * with the server.
 */
public class TCPClient extends AbstractClient {
    // Time out duration, currently set to 5 seconds
    private static final int TIMEOUT_INTERVAL = 5000;

    public void startClient(String serverIP, int serverPort) {
        Socket socket = null;
        try {
            socket = new Socket(serverIP, serverPort);
            socket.setSoTimeout(TIMEOUT_INTERVAL);
            System.out.println("[" + getTimestamp() + "] => " + "Connected to the server");
        } catch (IOException e) {
            System.out.println("[" + getTimestamp() + "]"
                + "Couldn't connect to server at mentioned IP and port");
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
            System.out.println("[" + getTimestamp() + "] => "
                + "Connection terminated by the server...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isGetAllRequest(String request) {
        return request.split("::")[2].equalsIgnoreCase("GET ALL");
    }

    private void handleHugeResponse(BufferedReader in) throws IOException {
        StringBuilder responseData = new StringBuilder();
        String response;
        while (((response = in.readLine()) != null) && !(response).equals("END")) {
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
                // Log response
                System.out.println("[" + getTimestamp() + "] => Response from server: "
                    + responseFromServer);
            }
        } catch (SocketTimeoutException e) {
            String[] strArr = request.split("::");
            String requestId = strArr[0];
            System.out.println("[" + getTimestamp() + "] => "
                + "Received no response from the server for request id : "+requestId);
        } catch (SocketException e) {
            System.out.println("[" + getTimestamp() + "] => "
              + "Server unavailable to handle requests");
        }
    }

    private void populateKeyValues(BufferedReader in, PrintWriter out) {
        final int NUM_KEYS = 45000;
        try {
            // PUT requests
            for (int i = 1; i <= NUM_KEYS*2; i++) {
                String key = Integer.toString(i);
                String value = Integer.toString(i * 10);
                String putString = generateUUID() + "::PUT::key" + key + "::value" + value;
                sendRequest(out, in, putString);
            }
            //GET requests
            for (int i = 1; i <= NUM_KEYS*2; i++) {
                String key = Integer.toString(i);
                String getString = generateUUID() + "::GET::key" + key;
                sendRequest(out, in, getString);
            }
            //DELETE requests
            for (int i = 5; i <= 10; i++) {
                String key = Integer.toString(i);
                String deleteString = generateUUID() + "::DELETE::key" + key;
                sendRequest(out, in, deleteString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
