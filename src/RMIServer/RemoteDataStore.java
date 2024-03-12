package RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RemoteDataStore extends UnicastRemoteObject implements IRemoteDataStore {
  private final Map<String, String> kvStore;
  protected RemoteDataStore() throws RemoteException {
    super();
    kvStore = new HashMap<>();
  }

  @Override
  public void put(String key, String value) throws RemoteException {
    kvStore.put(key, value);
  }

  @Override
  public String get(String key) throws RemoteException {
    return kvStore.get(key);
  }

  @Override
  public String delete(String key) throws RemoteException {
    return kvStore.remove(key);
  }
}
