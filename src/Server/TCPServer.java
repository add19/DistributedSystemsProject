package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This represents a TCP based server which listens at a given port number for TCP client requests.
 */
public class TCPServer extends AbstractServer {

    @Override
    public void listen(int portNumber) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("[" + getTimestamp() + "]=> " + "Server is listening on port " + portNumber);
            while (true) {
                // Start listening to client requests and creating client socket
                Socket clientSocket = serverSocket.accept();
                System.out.println("Request originating from [" + clientSocket.getInetAddress() + "] @ [" + getTimestamp() + "]=> " + "Client connected");

                try {
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // log information when client closes connection
                    clientSocket.close();
                    System.out.println("[" + getTimestamp() + "]=> " + "Client disconnected: " + clientSocket.getInetAddress());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRequest(Socket clientSocket) throws IOException {
        try (
          BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // log client request information
                System.out.println("Request originating from [" + clientSocket.getInetAddress() + "] @ [" + getTimestamp() + "]=> " + inputLine);

                String[] parsedTokens = parseRequest(inputLine);
                if(parsedTokens.length == 0) {
                    // return an error string.
                    out.println("Invalid request format");
                    continue;
                }

                // get information from the key value store
                String response = processRequest(parsedTokens);
                if(parsedTokens[2].equalsIgnoreCase("GET ALL")) {
                    String[] responseKeyVals = response.split("\n");
                    for(int i=1; i<responseKeyVals.length; i++) {
                        out.println(responseKeyVals[i]);
                    }
                    out.println("END");
                } else {
                    // write back the response to the client
                    out.println(response);
                    // log the response information
                    System.out.println("Response originating for [" + clientSocket.getInetAddress() + "] @ [" + getTimestamp() + "]=> " + response);
                }
            }
        } catch (IOException e) {
            // Log information about timed out requests.
            System.err.println("Timeout occurred. Server did not respond within the specified time.");
            System.out.println("Response time out from [" + clientSocket.getInetAddress() + "] @ [" + getTimestamp() + "]");
        }
    }
}
 