package RMIClient;

import Client.AbstractClient;
import RMIServer.IRemoteDataStore;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientWorker extends AbstractClient {

  @Override
  protected void displayUserChoices() {
    System.out.println("Specify operation:");
    System.out.println("Input [1] -> PUT");
    System.out.println("Input [2] -> GET");
    System.out.println("Input [3] -> DELETE");
    System.out.print("Enter your choice: ");
  }

  private void populateKeyValue(IRemoteDataStore remoteObj) throws RemoteException {
    System.out.println("[ " + getTimestamp() + " ]" + " => Pre-populating key value store");
    for(int i=0; i<100; i++) {
      String key = "KEY::" + i;
      String value = "VALUE::" + i;
      remoteObj.put(key, value);
    }

    System.out.println("[ " + getTimestamp() + " ]" + " => Doing Gets on pre-populated data");
    for(int i=0; i<100; i++) {
      String key = "KEY::" + i;
      String value = remoteObj.get(key);
      System.out.println("Tried Key: " + key + " Response: " + value);
    }

    System.out.println("[ " + getTimestamp() + " ]" + " => Deleting first 5 keys");
    for(int i=0; i<5; i++) {
      String key = "KEY::" + i;
      String value = remoteObj.delete(key);
      System.out.println("Deleted Key: " + key);
    }

    System.out.println("[ " + getTimestamp() + " ]" + " => Fetching first 5 keys");
    for(int i=0; i<5; i++) {
      String key = "KEY::" + i;
      String value = remoteObj.get(key);
      System.out.println("Tried Key: " + key + " Response: " + value);
    }

    System.out.println("[ " + getTimestamp() + " ]" + " => Fetching rest of the 95 keys");
    for(int i=5; i<100; i++) {
      String key = "KEY::" + i;
      String value = remoteObj.get(key);
      System.out.println("Tried Key: " + key + " Response: " + value);
    }
  }

  @Override
  public void startClient(String serverIp, int portNum) {
    IRemoteDataStore remoteObj = null;

    try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
      Registry registry = LocateRegistry.getRegistry("localhost", portNum);
      remoteObj = (IRemoteDataStore) registry.lookup("kvstore");
      populateKeyValue(remoteObj);

      while(true) {
        displayUserChoices();

        String choice = userInput.readLine();
        switch (choice) {
          case "1":
            String key = getKey(userInput);
            String value = getValue(userInput);
            remoteObj.put(key, value);
            System.out.println("[ " + getTimestamp() + " ]" + " => Key " + key + " stored with value " + value);
            break;
          case "2":
            key = getKey(userInput);
            String answer = remoteObj.get(key);
            System.out.println("[ " + getTimestamp() + " ]" + " => Response for GET key : " + key + " => " + answer);
            break;
          case "3":
            key = getKey(userInput);
            System.out.println("[ " + getTimestamp() + " ] => " + remoteObj.delete(key));
            break;
          default:
            System.out.println("Invalid choice. Please enter 1, 2, 3");
        }
      }
    } catch (NotBoundException | MalformedURLException | RemoteException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
