package Server;

/**
 * Represents the key value store and the operations supported for the server to utilize.
 */
public interface IKeyValueStore {
  /**
   * Inserts a given key and assigns it a given value in the data store.
   *
   * @param key The key to be stored.
   * @param value The associated value to be stored.
   */
  void put(String key, String value);

  /**
   * Gets the value for a given key from the data store.
   * @param key the key whose value is to be fetched.
   * @return A message containing the corresponding value stored against the key in the data store.
   */
  String get(String key);

  /**
   * Gets all the key value elements from the store.
   * @return A message containing the corresponding value stored against the key in the data store.
   */
  String getAll();

  /**
   * Deletes the given key and its corresponding value from the data store.
   * @param key the key to be deleted.
   * @return A message indicating whether the operation is successful or not.
   */
  String delete(String key);

  /**
   * Deletes all the keys and its corresponding values from the data store.
   */
  void deleteAll();
}