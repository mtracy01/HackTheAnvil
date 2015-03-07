package salmon.com.hellosoundboard;

/**
 * Created by Tim on 3/7/2015.
 */
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast(messageEvent.getPath());
        //Log.d("GOT MSG", "ANROID PHONE");
        MainActivity.playSound();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}