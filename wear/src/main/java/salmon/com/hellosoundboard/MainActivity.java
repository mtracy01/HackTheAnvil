package salmon.com.hellosoundboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "Wear Main Act";
    private TextView mTextView;
    boolean down = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                final ImageButton button = (ImageButton) findViewById(R.id.imageButton);
                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        String s = "";
                        s += "action=" + event.getAction();
                        s += ", X=" + event.getX();
                        s += ", Y=" + event.getY();
                        Log.d(TAG, s);
                        if(!down){
                            button.setImageResource(R.drawable.btn_red_down);
                            down = true;
                        }
                        else{
                            button.setImageResource(R.drawable.btn_red_up);
                            down = false;
                        }
                        return true;
                    }
                });
            }
        });

    }

}
