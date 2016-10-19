package dome.ninebox.com.androidmonkey.model;

/**
 * Created by Administrator on 2016/5/4.
 */
public class MatchDetails{

    private  long match_id;
    private  long account_id;
    private  long steam_id;
    private  long start_time;

    private  int player_slot;
    private  int hero_id;


    private  String item_0;
    private  String item_1;
    private  String item_2;
    private  String item_3;
    private  String item_4;
    private  String item_5;

    private  int kills;
    private  int deaths;
    private  int assists;

    private  int hero_damage;
    private  int tower_damage;
    private  int hero_healing;

    private  int level;

    private String imageUrl;
    private String avatar;
    private String person_name;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    //  private  List<?>String ability_upgrades;暂时不用 加点

    private  int gold_per_min;
    private  int xp_per_min;

    private  int radiant_win;  //false夜宴胜利 true天辉胜利


    public long getMatch_id() {
        return match_id;
    }

    public void setMatch_id(long match_id) {
        this.match_id = match_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
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

    public String getItem_0() {
        return item_0;
    }

    public void setItem_0(String item_0) {
        this.item_0 = item_0;
    }

    public String getItem_1() {
        return item_1;
    }

    public void setItem_1(String item_1) {
        this.item_1 = item_1;
    }

    public String getItem_2() {
        return item_2;
    }

    public void setItem_2(String item_2) {
        this.item_2 = item_2;
    }

    public String getItem_3() {
        return item_3;
    }

    public void setItem_3(String item_3) {
        this.item_3 = item_3;
    }

    public String getItem_4() {
        return item_4;
    }

    public void setItem_4(String item_4) {
        this.item_4 = item_4;
    }

    public String getItem_5() {
        return item_5;
    }

    public void setItem_5(String item_5) {
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

    public int getRadiant_win() {
        return radiant_win;
    }

    public void setRadiant_win(int radiant_win) {
        this.radiant_win = radiant_win;
    }

    public long getSteam_id() {
        return steam_id;
    }

    public void setSteam_id(long steam_id) {
        this.steam_id = steam_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    @Override
    public String toString() {
        return "MatchDetails{" +
                "match_id='" + match_id + '\'' +
                ", account_id='" + account_id + '\'' +
                ", start_time='" + start_time + '\'' +
                ", player_slot=" + player_slot +
                ", hero_id=" + hero_id +
                ", item_0=" + item_0 +
                ", item_1=" + item_1 +
                ", item_2=" + item_2 +
                ", item_3=" + item_3 +
                ", item_4=" + item_4 +
                ", item_5=" + item_5 +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", assists=" + assists +
                ", hero_damage=" + hero_damage +
                ", tower_damage=" + tower_damage +
                ", hero_healing=" + hero_healing +
                ", level=" + level +
                ", gold_per_min=" + gold_per_min +
                ", xp_per_min=" + xp_per_min +
                ", radiant_win=" + radiant_win +
                '}';
    }
}
