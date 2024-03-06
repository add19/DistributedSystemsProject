package Client;

/**
 * This is the start-up class for the TCP based client.
 */
public class ClientAppTCP {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Correct Usage: java ClientAppTCP [server-ip] [server-port]");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    IClient client = new TCPClient();
    client.startClient(serverIP, serverPort);
  }
}
