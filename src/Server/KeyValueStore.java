package Server;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the actual key value data store with all the associated operations that
 * can be performed on it.
 */
class KeyValueStore implements IKeyValueStore {
  private final Map<String, String> store;

  /**
   * Initializes the key value data store.
   */
  public KeyValueStore() {
    this.store = new HashMap<>();
  }

  /**
   * Inserts a given key and assigns it a given value in the data store.
   *
   * @param key The key to be stored.
   * @param value The associated value to be stored.
   */
  @Override
  public void put(String key, String value) {
    store.put(key, value);
  }

  /**
   * Gets the value for a given key from the data store.
   * @param key the key whose value is to be fetched.
   * @return A message containing the corresponding value stored against the key in the data store.
   */
  @Override
  public String get(String key) {
    return store.get(key);
  }

  @Override
  public String getAll() {
    if(store.isEmpty()) {
      return "-1";
    }
    StringBuilder sb = new StringBuilder(store.size() + "\n");
    for(Map.Entry<String, String> entry:store.entrySet()) {
      sb.append("key:").append(entry.getKey()).append(" value:").append(entry.getValue()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Deletes the given key and its corresponding value from the data store.
   * @param key the key to be deleted.
   * @return A message indicating whether the operation is successful or not.
   */
  @Override
  public String delete(String key) {
    return store.remove(key);
  }

  @Override
  public void deleteAll() {
    store.clear();
  }
}
