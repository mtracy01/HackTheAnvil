package salmon.com.hellosoundboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private static final String TAG = "Wear Main Act";
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private TextView mTextView;
    boolean down = false;
   // GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;

    public static final String PLAY_SOUND = "PLAY";
    String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = getGoogleApiClient(this);
        retrieveDeviceNode();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        //retrieveDeviceNode();
        //getGoogleApiClient(this);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                final ImageButton button = (ImageButton) findViewById(R.id.imageButton);
                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if(!down){
                            button.setImageResource(R.drawable.btn_red_down);
                            down = true;
                        }
                        else{
                            button.setImageResource(R.drawable.btn_red_up);
                            down = false;
                        }
                        send();
                        return true;
                    }
                });
            }
        });

    }

    //** Method 2**/
    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }
    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }
                client.disconnect();
            }
        }).start();
    }
    private void send() {
        if (nodeId != null) {
            Log.d("SENT: " + nodeId.toString(),TAG);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, PLAY_SOUND, null);
                    client.disconnect();
                }
            }).start();
        }
    }
}
