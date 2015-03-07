package salmon.com.hellosoundboard;

/**
 * Created by Matthew on 3/7/2015.
 */

public class SoundObject{
    private String name;
    private String url;

    public SoundObject(String name,String url){
        this.name=name;
        this.url=url;
    }
    public String getName(){return name;}
    public String getUrl(){return url;}
}