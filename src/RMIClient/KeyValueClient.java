package RMIClient;

import Client.AbstractClient;

public class KeyValueClient {

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Correct Usage: java RMIClient.KeyValueClient [server ip] [port]");
      System.exit(1);
    }

    AbstractClient abstractClient = new ClientWorker();
    int portNo = Integer.parseInt(args[1]);

    abstractClient.startClient(args[0], portNo);
  }
}
