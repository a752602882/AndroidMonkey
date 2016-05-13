package dome.ninebox.com.androidmonkey.model;

import java.util.Observable;

/**
 * Created by Administrator on 2016/5/4.
 */
//被监听者，数据发生改变时
public class MatchDetails {


    private  long match_id;



    private  long account_id;
    private  int start_time;
    private  int player_slot;
    private  int hero_id;


    private  int item_0;
    private  int item_1;
    private  int item_2;
    private  int item_3;
    private  int item_4;
    private  int item_5;

    private  int kills;
    private  int deaths;
    private  int assists;

    private  int hero_damage;
    private  int tower_damage;
    private  int hero_healing;

    private  int level;

    //  private  List<?>String ability_upgrades;暂时不用 加点

    private  int gold_per_min;
    private  int xp_per_min;

    private  boolean radiant_win;  //false夜宴胜利 true天辉胜利


    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getMatch_id() {
        return match_id;
    }

    public void setMatch_id(long match_id) {
        this.match_id = match_id;

    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getPlayer_slot() {
        return player_slot;
    }

    public void setPlayer_slot(int player_slot) {
        this.player_slot = player_slot;
    }

    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public int getItem_0() {
        return item_0;
    }

    public void setItem_0(int item_0) {
        this.item_0 = item_0;
    }

    public int getItem_1() {
        return item_1;
    }

    public void setItem_1(int item_1) {
        this.item_1 = item_1;
    }

    public int getItem_2() {
        return item_2;
    }

    public void setItem_2(int item_2) {
        this.item_2 = item_2;
    }

    public int getItem_3() {
        return item_3;
    }

    public void setItem_3(int item_3) {
        this.item_3 = item_3;
    }

    public int getItem_4() {
        return item_4;
    }

    public void setItem_4(int item_4) {
        this.item_4 = item_4;
    }

    public int getItem_5() {
        return item_5;
    }

    public void setItem_5(int item_5) {
        this.item_5 = item_5;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getHero_damage() {
        return hero_damage;
    }

    public void setHero_damage(int hero_damage) {
        this.hero_damage = hero_damage;
    }

    public int getTower_damage() {
        return tower_damage;
    }

    public void setTower_damage(int tower_damage) {
        this.tower_damage = tower_damage;
    }

    public int getHero_healing() {
        return hero_healing;
    }

    public void setHero_healing(int hero_healing) {
        this.hero_healing = hero_healing;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGold_per_min() {
        return gold_per_min;
    }

    public void setGold_per_min(int gold_per_min) {
        this.gold_per_min = gold_per_min;
    }

    public int getXp_per_min() {
        return xp_per_min;
    }

    public void setXp_per_min(int xp_per_min) {
        this.xp_per_min = xp_per_min;
    }

    public boolean isRadiant_win() {
        return radiant_win;
    }

    public void setRadiant_win(boolean radiant_win) {
        this.radiant_win = radiant_win;
    }

    @Override
    public String toString() {
        return "MatchDetails [setMatch_id="+match_id;
    }
}
