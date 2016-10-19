package dome.ninebox.com.androidmonkey.db;


import java.util.List;

import dome.ninebox.com.androidmonkey.model.Heroes;
import dome.ninebox.com.androidmonkey.model.Items;
import dome.ninebox.com.androidmonkey.model.MatchDetails;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface DotaMaxDAO {




    /**
     * 将hero实例存储到数据库。
     */
     void insertHeroes(List<Heroes> heroList);

    /**
     * 将Item实例存储到数据库。
     */
    void insertItems(List<Items> itemsList);


    /**
     * 加载英雄列表
    * @return
            */
     Heroes getHeroes(int id);






   void insertMatch(MatchDetails match);

    /**
     * 加载数据库里已有的数据
     * @param id
     * @return
     */
    MatchDetails getMatch(long id);

    /**
     * 加载用户的数据
     * @param account_id
     * @return
     */
    List<MatchDetails> getMatchByAccountId(long account_id);


    String getItemsNameById(String item_id);

}
