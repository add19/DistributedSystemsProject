package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class AbstractClient implements IClient {
  public String generateRequestFromUserChoice(BufferedReader userInput) throws IOException {
    System.out.println("Specify operation:");
    System.out.println("Input [1] -> PUT");
    System.out.println("Input [2] -> GET");
    System.out.println("Input [3] -> GET ALL");
    System.out.println("Input [4] -> DELETE");
    System.out.println("Input [5] -> DELETE ALL");
    System.out.print("Enter your choice: ");

    String choice = userInput.readLine();

    String request = "";
    switch (choice) {
      case "1":
        request = generatePutRequest(userInput);
        break;
      case "2":
        request = generateGetRequest(userInput);
        break;
      case "3":
        request = generateGetAllRequest();
        break;
      case "4":
        request = generateDeleteRequest(userInput);
        break;
      case "5":
        request = generateDeleteAllRequest();
        break;
      default:
        System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or 5.");
    }
    return request;
  }

  private String generateGetRequest(BufferedReader userInput) throws IOException {
    System.out.print("Please enter the key (integer): ");
    String key = userInput.readLine();
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "GET" + "::" + key;
  }

  private String generateGetAllRequest() throws IOException {
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "GET ALL";
  }

  private String generateDeleteAllRequest() throws IOException {
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "DELETE ALL";
  }

  private String generatePutRequest(BufferedReader userInput) throws IOException {
    System.out.print("Please enter the key: ");
    String key = userInput.readLine();
    System.out.print("Please enter the value for the key: ");
    String value = userInput.readLine();
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "PUT" + "::" + key + "::" + value;
  }

  private String generateDeleteRequest(BufferedReader userInput) throws IOException {
    System.out.print("Please enter the key (integer): ");
    String key = userInput.readLine();
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "DELETE" + "::" + key;
  }

  protected String getTimestamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }
}
