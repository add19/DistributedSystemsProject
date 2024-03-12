package RMIServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class KeyValueServer {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Correct Usage: java RMIServer.KeyValueServer [port]");
      System.exit(1);
    }

    int portNo = Integer.parseInt(args[0]);
    try {
      IRemoteDataStore ds = new RemoteDataStore();
      LocateRegistry.createRegistry(portNo);
      Naming.rebind("rmi://localhost:" + portNo +
        "/remoteserver",ds);
    } catch (RemoteException | MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
