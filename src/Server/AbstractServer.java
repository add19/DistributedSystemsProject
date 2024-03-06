package Server;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract class that implements common functionalities like processing key value store read/write
 * for any type of server.
 */
public abstract class AbstractServer implements IServer {
  private static final IKeyValueStore keyValueStore = new KeyValueStore();

  public String[] parseRequest(String inputLine) {
    String[] tokens = inputLine.split("::");
    if (tokens.length < 3) {
      return new String[0];
    }
    return tokens;
  }

  public String processRequest(String[] requestParams) {
    String requestId = requestParams[0];
    String function = requestParams[2];
    String value = requestParams.length > 4 ? requestParams[4] : null;

    switch (function.toUpperCase()) {
      case "PUT":
        String key = requestParams[3];
        if (value == null) {
          return requestId + ": PUT operation requires a value";
        }
        keyValueStore.put(key, value);
        return requestId + ": Key '" + key + "' stored with value '" + value + "'";
      case "GET":
        key = requestParams[3];
        String storedValue = keyValueStore.get(key);
        return (storedValue != null) ? (requestId + ": Value for key '" + key + "': " + storedValue)
            : (requestId + ": Key '" + key + "' not found");
      case "GET ALL":
        String response = keyValueStore.getAll();
        return response.equals("-1") ? requestId + ": -1" : (requestId + ":" + response);
      case "DELETE":
        key = requestParams[3];
        String removedValue = keyValueStore.delete(key);
        return (removedValue != null) ? (requestId + ": Deleted key '" + key + "' with value '" + removedValue + "'")
            : requestId + ": Key '" + key + "' not found";
      case "DELETE ALL":
        keyValueStore.deleteAll();
        return requestId + ": Deleted All keys ";
      default:
        return requestId + ": Unsupported operation: " + function;
    }
  }

  protected String getTimestamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }
}
