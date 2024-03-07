package Server;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the actual key value data kvStore with all the associated operations that
 * can be performed on it.
 */
class DataStore implements IDataStore {
  private final Map<String, String> kvStore;

  /**
   * Initializes the key value data kvStore.
   */
  public DataStore() {
    this.kvStore = new HashMap<>();
  }

  /**
   * Inserts a given key and assigns it a given value in the data kvStore.
   *
   * @param key The key to be stored.
   * @param value The associated value to be stored.
   */
  @Override
  public void put(String key, String value) {
    kvStore.put(key, value);
  }

  /**
   * Gets the value for a given key from the data kvStore.
   * @param key the key whose value is to be fetched.
   * @return A message containing the corresponding value stored against the key in the data kvStore.
   */
  @Override
  public String get(String key) {
    return kvStore.get(key);
  }

  @Override
  public String getAll() {
    if(kvStore.isEmpty()) {
      return "-1";
    }
    StringBuilder sb = new StringBuilder(kvStore.size() + "\n");
    for(Map.Entry<String, String> entry: kvStore.entrySet()) {
      sb.append("key:").append(entry.getKey()).append(" value:").append(entry.getValue()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Deletes the given key and its corresponding value from the data kvStore.
   * @param key the key to be deleted.
   * @return A message indicating whether the operation is successful or not.
   */
  @Override
  public String delete(String key) {
    return kvStore.remove(key);
  }

  @Override
  public void deleteAll() {
    kvStore.clear();
  }
}