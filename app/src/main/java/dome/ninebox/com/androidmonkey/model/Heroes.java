package dome.ninebox.com.androidmonkey.model;

/**
 * Created by Administrator on 2016/5/4.
 */
public class Heroes {

    private  int id;
    private  String  name;
    private  String  localized_name;

    public String getLocalized_name() {
        return localized_name;
    }

    public void setLocalized_name(String localized_name) {
        this.localized_name = localized_name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
