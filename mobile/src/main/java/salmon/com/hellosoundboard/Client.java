package salmon.com.hellosoundboard;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * Created by Matthew on 3/7/2015.
 */
public class Client {

    private static final String TAG= "Client";  //debug tag

    private static final String GETSOUNDS = "GETSOUNDS";
    private static final int CONNECTION_TIMEOUT = 10000; // 10 seconds
    private static final String API_ENDPOINT_URL = "http://tracy94.com:80";


    public Client(){
    }

    private HttpClient initClient(){
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);
        return client;
    }

    /**
     * Create a string from an InputStream
     *
     * This uses a clever trick: It tokenizes the string based on the delimiter for
     * the beginning of a string. Thus, it effectively tokenizes the string into one
     * long token.
     *
     * Based on http://stackoverflow.com/a/5445161
     *
     * @param is The input stream
     * @return A string containing the content of the stream
     */
    private String inputStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is);
        Scanner tokenizer = scanner.useDelimiter("\\A");
        String str = tokenizer.hasNext() ? tokenizer.next() : "";
        scanner.close();
        return str;
    }

    /**
     * Send a command to the server for execution and return the server response
     *
     * This method serves as an abstraction to send a message to the server and return
     * exactly what the server sends back.
     *
     * @param command The command to execut
     * @return The server's response, or an empty string if the response could not be retrieved
     */
    private String executeServerCommand(String command) {
        HttpClient client = initClient();
        HttpPost request = new HttpPost();

        try {
            Log.v(TAG, "Executing command: " + command);
            request.setURI(new URI(API_ENDPOINT_URL));
            request.setEntity(new StringEntity(command, "UTF-8"));

            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() != 200) {
                return ""; // An error of some sort occurred
            }

            return inputStreamToString(response.getEntity().getContent());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
