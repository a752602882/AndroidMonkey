package dome.ninebox.com.androidmonkey.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import dome.ninebox.com.androidmonkey.R;
import dome.ninebox.com.androidmonkey.view.RoundedImageView;

/**
 * Created by Administrator on 2016/4/25.
 */
    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public  static  int TAG =0x6666;
    public  static  int TAG2 =0x5555;

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

    public TextView start_time;
    public LinearLayout radiant_win;
  /*  public ImageButton btnLike;
    public TextView person_like;*/

/*

    @BindView(R.id.hero_image)
    ImageView hero_image;
    @BindView(R.id.avatar_image)
    RoundedImageView avatar_image;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.hurt)
    TextView hurt;
    @BindView(R.id.goods_item0)
    ImageView item0;
    @BindView(R.id.goods_item1)
    ImageView item1;
    @BindView(R.id.goods_item2)
    ImageView item2;
    @BindView(R.id.goods_item3)
    ImageView item3;
    @BindView(R.id.goods_item4)
    ImageView item4;
    @BindView(R.id.goods_item5)
    ImageView item5;
    @BindView(R.id.kda_info)
    TextView kda_info;

    @BindView(R.id.kda)
    TextView kda;
    @BindView(R.id.start_time)
    TextView start_time;
    @BindView(R.id.radiant_win)
    LinearLayout radiant_win;
*/




    public MyRecyclerViewHolder(View itemView) {

        super(itemView);

    //    ButterKnife.bind(this, itemView);

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


        radiant_win = (LinearLayout) itemView.findViewById(R.id.radiant_win);
        start_time = (TextView) itemView.findViewById(R.id.start_time);
      //  ivFeedBottom = (ImageView) itemView.findViewById(R.id.ivFeedBottom);

    }

    @Override
    public void onClick(View view) {

     /*    if (view.getId() == btnLike.getId()&&view.getTag().equals("second")){
             person_like.setText("1 like");
             btnLike.setTag(R.id.tag_first,"frist");
         }else {
             person_like.setText("0 like");
             btnLike.setTag(R.id.tag_second,"second");
         }*/
    }
}
