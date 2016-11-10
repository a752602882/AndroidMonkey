package dome.ninebox.com.androidmonkey.model;

/**
 * Created by Administrator on 2016/11/1.
 */

public class User {

    private int _id;
    private long steam_id;
    private String name;
    private String avatarmedium;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public long getSteam_id() {
        return steam_id;
    }

    public void setSteam_id(long steam_id) {
        this.steam_id = steam_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarmedium() {
        return avatarmedium;
    }

    public void setAvatarmedium(String avatarmedium) {
        this.avatarmedium = avatarmedium;
    }

    public User() {

    }

    public User(long steam_id, String name, String avatarmedium) {

        this.steam_id = steam_id;
        this.name = name;
        this.avatarmedium = avatarmedium;
    }
}
