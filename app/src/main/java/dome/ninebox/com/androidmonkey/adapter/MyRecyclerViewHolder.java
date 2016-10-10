package dome.ninebox.com.androidmonkey.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import dome.ninebox.com.androidmonkey.R;
/**
 * Created by Administrator on 2016/4/25.
 */
    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {


    public ImageView hero_image;
    public ImageView ivFeedBottom;
    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        hero_image = (ImageView) itemView.findViewById(R.id.hero_image);
      //  ivFeedBottom = (ImageView) itemView.findViewById(R.id.ivFeedBottom);
    }
}
