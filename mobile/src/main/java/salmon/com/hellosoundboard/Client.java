package salmon.com.hellosoundboard;

import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Matthew on 3/7/2015.
 */
public class Client {

    private static final String TAG= "Client";  //debug tag

    private static final String GETSOUNDS = "GETSOUNDS";
    private static final int CONNECTION_TIMEOUT = 10000; // 10 seconds
    private static final String API_ENDPOINT_URL = "http://tracy94.com:2048/?page=1";


    public Client(){
    }

    private HttpClient initClient(){
        HttpClient client = new DefaultHttpClient();
        //HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);
        //HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);
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
     * @param command The command to execute
     * @return The server's response, or an empty string if the response could not be retrieved
     */
    private List executeServerCommand(String command) {
        HttpClient client = new DefaultHttpClient();//initClient();
        HttpGet request = new HttpGet();

        try {
            Log.v(TAG, "Executing command: " + command);
            //request.setURI(new URI(API_ENDPOINT_URL));


            //request.setEntity(new UrlEncodedFormEntity(pairs));
            request.setURI(new URI(API_ENDPOINT_URL));
            HttpResponse response = client.execute(request);

            // ;

            /*if(response.getStatusLine().getStatusCode() != 200) {
                //return ""; // An error of some sort occurred
            }*/

            JsonReader reader = new JsonReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
            List ret = readMessagesArray(reader);
            return ret;
            //return null;
            //return inputStreamToString(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"Connection refused!");
            return null;
        }


    }

    public List readMessagesArray(JsonReader reader) throws IOException {
        List messages = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public SoundObject readMessage(JsonReader reader) throws IOException {
        String name= null;
        String url= null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name2 = reader.nextName();
            if (name2.equals("name")) {
                name= reader.nextString();
            } else if (name2.equals("url")) {
                url = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new SoundObject(name,url);
    }


    public List getPage(int i){
        String command = "POST page=1";
        List response = executeServerCommand(command);
        return response;
    }
}

