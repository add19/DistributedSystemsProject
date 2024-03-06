package Client;

/**
 * This is the start-up class for the UDP based client.
 */
public class ClientAppUDP {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println(
        "Correct Usage: java ClientAppUDP [server-ip] [server-port]");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    IClient client = new UDPClient();
    client.startClient(serverIP, serverPort);
  }
}
