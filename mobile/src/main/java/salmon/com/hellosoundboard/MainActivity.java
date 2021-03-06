package salmon.com.hellosoundboard;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity {

    //Debug log tag
    private final String TAG = "MainActivity";

    private ListView             SoundsList;    //UI for our list of sounds
    private List                 SoundObjects;  //List of JSONObjects
    private ArrayList<String>    SoundNames;    //String names of sounds
    public static Context             context;
    public static SoundObject soundObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws java.lang.IllegalMonitorStateException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoundsList = (ListView)findViewById(R.id.listView);
        SoundNames = new ArrayList<>();
        context=getApplicationContext();
        ListenerService ls = new ListenerService();
        //create our client and get our soundObjects list
        //Client client = new Client();
        AsyncTask<Void, Void, Integer> getTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                /*
                Client client = new Client();
                SoundObjects=client.getPage(1);
                */
                List<SoundObject> sounds = new ArrayList<SoundObject>();
                Scanner in = new Scanner(getResources().openRawResource(R.raw.sounds));
                in.useDelimiter("\\Z");
                String json = in.next();
                in.close();
                try {
                    JSONArray arr = new JSONArray(json);
                    for(int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String name = obj.getString("name"),
                                url = obj.getString("url");
                        SoundObject sObj = new SoundObject(name, url);
                        sounds.add(sObj);
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                SoundObjects = sounds;


                return 1;
            }
            @Override
            protected void onPostExecute(Integer integer) {
                if(SoundObjects==null){
                    //Toast toast= Toast.makeText(this,"Server Error")
                    Log.e(TAG,"Null SoundObjects returned");
                    return;
                }
                int size = SoundObjects.size();
                Log.v(TAG,"SIZE= " + size);
                for(int i=0;i<size;i++){
                    SoundObject temp = (SoundObject)SoundObjects.get(i);
                    String name;
                    name = temp.getName();
                    Log.v(TAG,name);
                    SoundNames.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, SoundNames);
                SoundsList.setAdapter(adapter);

                SoundsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        soundObject = (SoundObject)SoundObjects.get(position);
                    }
                });

            }
        };
        getTask.execute();
        /*try{
            wait(5000);     //wait 5 seconds for the synchronous task to execute
        }
        catch(InterruptedException e){
            e.printStackTrace();
            Log.v(TAG,"Interrupt exception!");
        }*/
        /*if(SoundNames!=null) {
            Log.e(TAG,"TRUE");

        }*/



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static void playSound(){
            try {

                final MediaPlayer player = new MediaPlayer();
                player.reset();

                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //Log.e(TAG,soundObject.getUrl());

                Uri uri = Uri.parse("http://" + MainActivity.soundObject.getUrl());
                player.setDataSource(MainActivity.context,uri);
                //player.prepare();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                player.prepare();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}
