package Client;

/**
 * This represents the client application.
 */
public interface IClient {

  /**
   * Starts client process to send request to server on given port and IP address
   * @param serverIp server's IP address to send request.
   * @param portNum server's port number on which it listens to client requests.
   */
  void startClient(String serverIp, int portNum);
}
