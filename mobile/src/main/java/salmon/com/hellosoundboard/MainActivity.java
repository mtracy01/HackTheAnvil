package salmon.com.hellosoundboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView SoundsList;    //UI for our list of sounds
    private List SoundObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoundsList = (ListView)findViewById(R.id.listView);

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
                /*if(check==1){
                    Intent intent = new Intent(LoginActivity.this,StudentActivity.class);
                    intent.putExtra("user",email);
                    startActivity(intent);
                }
                else if(check==2){
                    Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                    intent.putExtra("user",email);
                    startActivity(intent);
                }
                else{
                    mEmailView.setError("Invalid username or password");
                    mEmailView.requestFocus();
                }
            }*/
            }
        };
        getTask.execute();

        

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
