package salmon.com.hellosoundboard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    //Debug log tag
    private final String TAG = "MainActivity";

    private ListView             SoundsList;    //UI for our list of sounds
    private List                 SoundObjects;  //List of JSONObjects
    private ArrayList<String>    SoundNames;    //String names of sounds
    private Context             context;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws java.lang.IllegalMonitorStateException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoundsList = (ListView)findViewById(R.id.listView);
        SoundNames = new ArrayList<>();
        context=getApplicationContext();

        //create our client and get our soundObjects list
        //Client client = new Client();
        AsyncTask<Void, Void, Integer> getTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                Client client = new Client();
                SoundObjects=client.getPage(1);
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
}
