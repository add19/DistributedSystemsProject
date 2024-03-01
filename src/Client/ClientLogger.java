package Client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This represents a logger for writing all the information/logs generated into client logs.
 */
public class ClientLogger {
    private static final String LOG_FILE = "client_log.txt";

    public static void log(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
