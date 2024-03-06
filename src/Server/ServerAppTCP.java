package Server;

/**
 * This class starts a TCP server by creating an instance of a TCP server.
 */
public class ServerAppTCP {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Correct Usage: java Server/ServerAppTCP [tcp-port]");
      System.exit(1);
    }

    int tcpPortNumber = Integer.parseInt(args[0]);
    IServer tcpServer = new TCPServer();
    tcpServer.listen(tcpPortNumber);
  }
}
