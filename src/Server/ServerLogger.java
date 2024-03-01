package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This represents a logger for writing all the information/logs generated into server level logs.
 */
public class ServerLogger {
    private static final String LOG_FILE = "server_log.txt";

    /**
     * This function writes the server messages to the LOG_FILE.
     * @param message the log information to be written.
     */
    public void log(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the request information along with the source of the client request.
     * @param clientAddress The origin of request passed to server.
     * @param request Request information to be written to logs.
     */
    public void logRequest(InetAddress clientAddress, String request) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Request from : " + clientAddress + ": " + request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Log server response sent to client.
     * @param clientAddress the address information to which the response is being sent.
     * @param response the actual response string sent to client.
     */
    public void logResponse(InetAddress clientAddress, String response) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Response to : " + clientAddress + ": " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This logs all the malformed datagram requests of a specified length.
     * @param clientAddress The origin of malformed datagram request passed to server.
     * @param packetLength Length of the malformed datagram packet.
     */
    public void logMalformedRequest(InetAddress clientAddress, int packetLength) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Received Malformed request from " + clientAddress + " of length " + packetLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This logs all the malformed requests; specifically for TCP servers.
     * @param clientAddress The origin of malformed request passed to server.
     */
    public void logMalformedRequest(InetAddress clientAddress) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Received Malformed request from " + clientAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
