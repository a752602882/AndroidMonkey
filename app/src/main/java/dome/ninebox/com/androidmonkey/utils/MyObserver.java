package dome.ninebox.com.androidmonkey.utils;

import java.util.Observable;
import java.util.Observer;

import dome.ninebox.com.androidmonkey.model.MatchDetails;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MyObserver  {

    private int i;
    private MatchDetails details;//观察的对象

    public MyObserver(int i){
        System.out.println("我是观察者---->" + i);
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public MatchDetails getDetails() {
        return details;
    }

    public void setDetails(MatchDetails details) {
        this.details = details;
    }




}
