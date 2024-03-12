package RMIClient;

import Client.AbstractClient;
import RMIServer.IRemoteDataStore;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientWorker extends AbstractClient {

  @Override
  public void startClient(String serverIp, int portNum) {
    IRemoteDataStore remoteObj = null;
    try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

      remoteObj = (IRemoteDataStore) Naming.lookup("rmi://localhost:" + portNum +
        "/remoteserver");

      while(true) {
        displayUserChoices();

        String choice = userInput.readLine();
        switch (choice) {
          case "1":
            String key = getKey(userInput);
            String value = getValue(userInput);
            remoteObj.put(key, value);
            System.out.println("Key " + key + " stored with value " + value);
            break;
          case "2":
            key = getKey(userInput);
            String answer = remoteObj.get(key);
            System.out.println("Value for key : " + key + " => " + answer);
            break;
          case "3":
            key = getKey(userInput);
            remoteObj.delete(key);
            System.out.println("Deleted key : " + key);
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
