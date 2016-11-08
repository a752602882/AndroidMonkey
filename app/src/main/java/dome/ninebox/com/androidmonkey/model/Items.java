package dome.ninebox.com.androidmonkey.model;

/**
 * Created by Administrator on 2016/10/14.
 */

public class Items {

    private int _id;
    private int cost;
    private String name;
    private int secret_shop;
    private int side_shop;
    private int recipe;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSecret_shop() {
        return secret_shop;
    }

    public void setSecret_shop(int secret_shop) {
        this.secret_shop = secret_shop;
    }

    public int getSide_shop() {
        return side_shop;
    }

    public void setSide_shop(int side_shop) {
        this.side_shop = side_shop;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }
}
