package dome.ninebox.com.androidmonkey.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import dome.ninebox.com.androidmonkey.R;
import dome.ninebox.com.androidmonkey.view.RoundedImageView;

/**
 * Created by Administrator on 2016/4/25.
 */
    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {


    public ImageView hero_image;
    public RoundedImageView avatar_image;


    public TextView user_name;
//    public TextView fight_percent;
    public TextView hurt;

    public ImageView item0;
    public ImageView item1;
    public ImageView item2;
    public ImageView item3;
    public ImageView item4;
    public ImageView item5;

    public TextView kda_info;
    public TextView kda;


    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        hero_image = (ImageView) itemView.findViewById(R.id.hero_image);
        user_name = (TextView) itemView.findViewById(R.id.user_name);
    //    fight_percent = (TextView) itemView.findViewById(R.id.fight_percent);
        hurt = (TextView) itemView.findViewById(R.id.hurt);

        item0 = (ImageView) itemView.findViewById(R.id.goods_item0);
        item1 = (ImageView) itemView.findViewById(R.id.goods_item1);
        item2 = (ImageView) itemView.findViewById(R.id.goods_item2);
        item3 = (ImageView) itemView.findViewById(R.id.goods_item3);
        item4 = (ImageView) itemView.findViewById(R.id.goods_item4);
        item5 = (ImageView) itemView.findViewById(R.id.goods_item5);

        kda_info = (TextView) itemView.findViewById(R.id.kda_info);
        kda = (TextView) itemView.findViewById(R.id.kda);


        avatar_image = (RoundedImageView) itemView.findViewById(R.id.avatar_image);


      //  ivFeedBottom = (ImageView) itemView.findViewById(R.id.ivFeedBottom);
    }
}
