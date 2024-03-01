package Server;

/**
 * This class starts a UDP server by creating an instance of a UDP server.
 */
public class ServerAppUDP {
    public static void main(String[] args) {
      if (args.length != 1) {
        System.out.println("Usage: java Server <udp-port>");
        System.exit(1);
      }

      int udpPortNumber = Integer.parseInt(args[0]);
      IServer server = new UDPServer();
      server.listen(udpPortNumber);
    }
}
