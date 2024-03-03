package Server;

import java.io.IOException;
import java.net.Socket;

/**
 * This interface represents a basic server that listens to clients and handles their respective
 * requests.
 */
public interface IServer {

  /**
   * Listens to client connections for incoming requests.
   */
  void listen(int portNumber);

  /**
   * Parses request params for the given Client socket.
   * @param clientSocket Client socket object.
   * @throws IOException in case of any IO failures from socket streams.
   */
  void handleRequest(Socket clientSocket) throws IOException;

  /**
   * Processes the input request parsed from handleRequest method.
   * @param tokens input request as parsed from handleRequest.
   */
  String processRequest(String[] tokens);
}
