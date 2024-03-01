package Client;

/**
 * This class starts an instance of a UDP client.
 */
public class ClientAppUDP {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println(
        "Usage: java ClientAppUDP <serverIP> <port>");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    IClient client = new UDPClient();
    client.startClient(serverIP, serverPort);
  }

}
