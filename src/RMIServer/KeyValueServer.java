package RMIServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class KeyValueServer {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Correct Usage: java RMIServer.KeyValueServer [port]");
      System.exit(1);
    }

    int portNo = Integer.parseInt(args[0]);
    try {
      RemoteDataStore obj = new RemoteDataStore();
      IRemoteDataStore stub = (IRemoteDataStore) UnicastRemoteObject.exportObject(obj, 0);
      Registry registry = LocateRegistry.createRegistry(portNo);

      registry.rebind("kvstore", stub);
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }
}
