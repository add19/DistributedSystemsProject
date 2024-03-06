package Server;

/**
 * This interface represents functionalities of a server that listens for client connections and
 * handles their respective requests.
 */
public interface IServer {

  /**
   * Listens for client connections on the specified port number.
   * @param portNumber the port number on which this client listens on.
   */
  void listen(int portNumber);

  /**
   * Processes input request already parsed into tokens.
   * @param tokens input request in parsed form.
   */
  String processRequest(String[] tokens);
}