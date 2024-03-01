package Client;


/**
 * This class starts an instance of a TCP client.
 */
public class ClientAppTCP {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: java ClientAppTCP <serverIP> <port>");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    IClient client = new TCPClient();
    client.startClient(serverIP, serverPort);
  }
}
