package RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteDataStore implements IRemoteDataStore {
  private final ConcurrentMap<String, String> kvStore;
  protected RemoteDataStore() throws RemoteException {
    super();
    kvStore = new ConcurrentHashMap<>();
  }

  @Override
  public void put(String key, String value) throws RemoteException {
    kvStore.put(key, value);
  }

  @Override
  public String get(String key) throws RemoteException {
    if(!kvStore.containsKey(key)) {
      return "Key " + key + " doesn't exist in the store";
    }
    return kvStore.get(key);
  }

  @Override
  public String delete(String key) throws RemoteException {
    if(kvStore.containsKey(key)) {
      kvStore.remove(key);
      return "Deleted key " + key;
    }
    return "Key " + key + " not found";
  }
}
