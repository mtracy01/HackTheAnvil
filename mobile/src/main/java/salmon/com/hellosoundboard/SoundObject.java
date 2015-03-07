package salmon.com.hellosoundboard;

import android.util.Log;

/**
 * Created by Matthew on 3/7/2015.
 */

public class SoundObject{
    private String name;
    private String url;

    public SoundObject(){

    }
    public SoundObject(String name,String url){
        this.name=name;
        this.url=url;
    }
    public String getName(){return name;}
    public String getUrl(){return url;}

    public void play(){
        Log.d("Play", "Android Phone");
    }

}