package Server;

import java.io.IOException;
import java.net.Socket;

/**
 * Abstract class that implements common functionalities like processing key value store read/write
 * for any type of server.
 */
public abstract class AbstractServer implements IServer {
  private static final IKeyValueStore keyValueStore = new KeyValueStore();
  static final ServerLogger serverLogger = new ServerLogger();

  public String[] parseRequest(String inputLine) {
    String[] tokens = inputLine.split("::");
    System.out.println(inputLine);
    if (tokens.length < 3) {
      return new String[0];
    }
    return tokens;
  }

  public String processRequest(String[] tokens) {
    String requestId = tokens[0];
    String operation = tokens[2];
    String value = tokens.length > 4 ? tokens[4] : null;

    switch (operation.toUpperCase()) {
      case "PUT":
        String key = tokens[3];
        if (value == null) {
          return requestId + ": PUT operation requires a value";
        }
        keyValueStore.put(key, value);
        return requestId + ": Key '" + key + "' stored with value '" + value + "'";
      case "GET":
        key = tokens[3];
        String storedValue = keyValueStore.get(key);
        return (storedValue != null) ? (requestId + ": Value for key '" + key + "': " + storedValue)
            : (requestId + ": Key '" + key + "' not found");
      case "GET ALL":
        String response = keyValueStore.getAll();
        return response.equals("-1") ? requestId + ": Empty Store '" : (requestId + ":" + response); // TODO: Test empty scenario
      case "DELETE":
        key = tokens[3];
        String removedValue = keyValueStore.delete(key);
        return (removedValue != null) ? (requestId + ": Deleted key '" + key + "' with value '" + removedValue + "'")
            : requestId + ": Key '" + key + "' not found";
      case "DELETE ALL":
        keyValueStore.deleteAll();
        return requestId + ": Deleted All keys '";
      default:
        return requestId + ": Unsupported operation: " + operation;
    }
  }

  @Override
  public void handleRequest(Socket clientSocket) throws IOException {
    serverLogger.logServerMessage("Unable to process request. Server handle request behavior undefined");
  }
}
