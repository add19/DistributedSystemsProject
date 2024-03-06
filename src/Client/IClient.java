package Client;

/**
 * This interface represents the client application and the required operations for any type of
 * client to start its application.
 */
public interface IClient {

  /**
   * Initiates client process to send and receive messages from server at the given IP and port
   *
   * @param serverIp IP address of the server to send request.
   * @param portNum server's port number on which it listens to client requests.
   */
  void startClient(String serverIp, int portNum);
}
